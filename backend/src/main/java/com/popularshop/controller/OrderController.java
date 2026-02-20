package com.popularshop.controller;

import com.popularshop.dto.*;
import com.popularshop.entity.User;
import com.popularshop.repository.UserRepository;
import com.popularshop.service.CouponService;
import com.popularshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("訂單建立成功", orderService.createOrder(userId, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(orderService.getUserOrders(userId, page, size)));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderById(userId, orderId)));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("訂單已取消", orderService.cancelUserOrder(userId, orderId)));
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
