package com.popularshop.controller;

import com.popularshop.dto.*;
import com.popularshop.entity.User;
import com.popularshop.repository.UserRepository;
import com.popularshop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getWishlist(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(wishlistService.getWishlist(userId)));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> add(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        Long userId = getUserId(userDetails);
        wishlistService.addToWishlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("已加入願望清單", null));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> remove(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        Long userId = getUserId(userDetails);
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("已從願望清單移除", null));
    }

    @GetMapping("/check/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> check(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(wishlistService.isInWishlist(userId, productId)));
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
