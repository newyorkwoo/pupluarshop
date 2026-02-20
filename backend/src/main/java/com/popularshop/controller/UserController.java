package com.popularshop.controller;

import com.popularshop.dto.*;
import com.popularshop.entity.User;
import com.popularshop.repository.UserRepository;
import com.popularshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(userService.getProfile(userId)));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("個人資料已更新", userService.updateProfile(userId, request)));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {
        Long userId = getUserId(userDetails);
        userService.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.success("密碼已更新", null));
    }

    @GetMapping("/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddresses(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(userService.getAddresses(userId)));
    }

    @PostMapping("/addresses")
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddressRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("地址已新增", userService.addAddress(userId, request)));
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success("地址已更新", userService.updateAddress(userId, addressId, request)));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId) {
        Long userId = getUserId(userDetails);
        userService.deleteAddress(userId, addressId);
        return ResponseEntity.ok(ApiResponse.success("地址已刪除", null));
    }

    private Long getUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
