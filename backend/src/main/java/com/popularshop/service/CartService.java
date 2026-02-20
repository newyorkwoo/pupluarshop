package com.popularshop.service;

import com.popularshop.dto.CartItemRequest;
import com.popularshop.dto.CartItemResponse;
import com.popularshop.entity.CartItem;
import com.popularshop.entity.Product;
import com.popularshop.entity.User;
import com.popularshop.exception.ResourceNotFoundException;
import com.popularshop.repository.CartItemRepository;
import com.popularshop.repository.ProductRepository;
import com.popularshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCart(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(CartItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartItemResponse addToCart(Long userId, CartItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        // Check if already in cart
        var existing = cartItemRepository.findByUserIdAndProductIdAndSize(userId, request.getProductId(),
                request.getSize());
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            return CartItemResponse.fromEntity(cartItemRepository.save(item));
        }

        CartItem item = CartItem.builder()
                .user(user)
                .product(product)
                .size(request.getSize())
                .quantity(request.getQuantity())
                .build();
        return CartItemResponse.fromEntity(cartItemRepository.save(item));
    }

    @Transactional
    public CartItemResponse updateQuantity(Long userId, Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .filter(i -> i.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));
        item.setQuantity(quantity);
        return CartItemResponse.fromEntity(cartItemRepository.save(item));
    }

    @Transactional
    public void removeItem(Long userId, Long itemId) {
        cartItemRepository.deleteByIdAndUserId(itemId, userId);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
