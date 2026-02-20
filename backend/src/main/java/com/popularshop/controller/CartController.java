package com.popularshop.controller;

import com.popularshop.dto.*;
import com.popularshop.entity.User;
import com.popularshop.repository.UserRepository;
import com.popularshop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(cartService.getCart(userId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CartItemResponse>> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CartItemRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("已加入購物車", cartService.addToCart(userId, request)));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(cartService.updateQuantity(userId, itemId, quantity)));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> removeItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId) {
        Long userId = getUserId(userDetails);
        cartService.removeItem(userId, itemId);
        return ResponseEntity.ok(ApiResponse.success("已從購物車移除", null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success("購物車已清空", null));
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
