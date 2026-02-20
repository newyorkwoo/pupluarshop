package com.popularshop.service;

import com.popularshop.dto.OrderRequest;
import com.popularshop.dto.OrderResponse;
import com.popularshop.dto.PageResponse;
import com.popularshop.entity.*;
import com.popularshop.exception.BadRequestException;
import com.popularshop.exception.ResourceNotFoundException;
import com.popularshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("購物車是空的");
        }

        // Calculate totals
        BigDecimal totalAmount = BigDecimal.ZERO;
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .status(Order.OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(Order.PaymentStatus.UNPAID)
                .shippingName(request.getShippingName())
                .shippingPhone(request.getShippingPhone())
                .shippingCity(request.getShippingCity())
                .shippingDistrict(request.getShippingDistrict())
                .shippingZipCode(request.getShippingZipCode())
                .shippingAddress(request.getShippingAddress())
                .note(request.getNote())
                .shippingFee(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .build();

        for (CartItem ci : cartItems) {
            Product product = ci.getProduct();
            BigDecimal price = product.getSalePrice() != null && product.getSalePrice().compareTo(BigDecimal.ZERO) > 0
                    ? product.getSalePrice()
                    : product.getPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(ci.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productName(product.getName())
                    .productImage(product.getImages() != null && !product.getImages().isEmpty()
                            ? product.getImages().get(0).getImageUrl()
                            : null)
                    .size(ci.getSize())
                    .quantity(ci.getQuantity())
                    .price(price)
                    .subtotal(subtotal)
                    .build();
            order.getItems().add(item);

            // Decrement stock
            product.setStock(Math.max(0, product.getStock() - ci.getQuantity()));
            productRepository.save(product);
        }

        // Apply coupon
        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            BigDecimal discount = applyCoupon(request.getCouponCode(), totalAmount);
            order.setDiscount(discount);
            order.setCouponCode(request.getCouponCode());
            totalAmount = totalAmount.subtract(discount);
        }

        // Shipping fee (free over 3000)
        if (totalAmount.compareTo(new BigDecimal("3000")) < 0) {
            order.setShippingFee(new BigDecimal("120"));
            totalAmount = totalAmount.add(new BigDecimal("120"));
        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        // Clear cart
        cartItemRepository.deleteByUserId(userId);

        return OrderResponse.fromEntity(order);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getUserOrders(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        List<OrderResponse> content = orders.getContent().stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
        return PageResponse.of(content, orders.getNumber(), orders.getSize(),
                orders.getTotalElements(), orders.getTotalPages(), orders.isLast());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return OrderResponse.fromEntity(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
        return OrderResponse.fromEntity(order);
    }

    @Transactional
    public OrderResponse cancelUserOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new BadRequestException("只有待處理的訂單可以取消");
        }
        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
        return OrderResponse.fromEntity(order);
    }

    // Admin
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getAllOrders(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orders;
        if (status != null && !status.isBlank()) {
            orders = orderRepository.findByStatus(Order.OrderStatus.valueOf(status), pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }
        List<OrderResponse> content = orders.getContent().stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
        return PageResponse.of(content, orders.getNumber(), orders.getSize(),
                orders.getTotalElements(), orders.getTotalPages(), orders.isLast());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        order.setStatus(Order.OrderStatus.valueOf(status));
        if ("CANCELLED".equals(status) || "REFUNDED".equals(status)) {
            order.setPaymentStatus(Order.PaymentStatus.REFUNDED);
        }
        orderRepository.save(order);
        return OrderResponse.fromEntity(order);
    }

    private BigDecimal applyCoupon(String code, BigDecimal total) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new BadRequestException("無效的優惠碼"));

        if (!coupon.isActive())
            throw new BadRequestException("優惠碼已停用");
        if (coupon.getStartDate() != null && coupon.getStartDate().isAfter(LocalDateTime.now()))
            throw new BadRequestException("優惠碼尚未生效");
        if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("優惠碼已過期");
        if (coupon.getMaxUses() != null && coupon.getUsedCount() >= coupon.getMaxUses())
            throw new BadRequestException("優惠碼已達使用上限");
        if (total.compareTo(coupon.getMinPurchase()) < 0)
            throw new BadRequestException("未達最低消費金額");

        BigDecimal discount;
        if (coupon.getType() == Coupon.CouponType.PERCENTAGE) {
            discount = total.multiply(coupon.getValue()).divide(BigDecimal.valueOf(100));
            if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
                discount = coupon.getMaxDiscount();
            }
        } else {
            discount = coupon.getValue();
        }

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        return discount;
    }

    private String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "PS" + datePart + randomPart;
    }
}
