package com.popularshop.dto;

import com.popularshop.entity.Order;
import com.popularshop.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String status;
    private String paymentStatus;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private BigDecimal discount;
    private String couponCode;
    private String paymentMethod;
    private String note;
    private ShippingInfo shippingInfo;
    private List<OrderItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingInfo {
        private String name;
        private String phone;
        private String city;
        private String district;
        private String zipCode;
        private String address;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDto {
        private Long id;
        private Long productId;
        private String productName;
        private String productImage;
        private String size;
        private int quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
    }

    public static OrderResponse fromEntity(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .totalAmount(order.getTotalAmount())
                .shippingFee(order.getShippingFee())
                .discount(order.getDiscount())
                .couponCode(order.getCouponCode())
                .paymentMethod(order.getPaymentMethod())
                .note(order.getNote())
                .shippingInfo(ShippingInfo.builder()
                        .name(order.getShippingName())
                        .phone(order.getShippingPhone())
                        .city(order.getShippingCity())
                        .district(order.getShippingDistrict())
                        .zipCode(order.getShippingZipCode())
                        .address(order.getShippingAddress())
                        .build())
                .items(order.getItems() != null
                        ? order.getItems().stream().map(OrderResponse::toItemDto).collect(Collectors.toList())
                        : List.of())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private static OrderItemDto toItemDto(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .productName(item.getProductName())
                .productImage(item.getProductImage())
                .size(item.getSize())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
