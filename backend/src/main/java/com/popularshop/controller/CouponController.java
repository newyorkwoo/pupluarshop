package com.popularshop.controller;

import com.popularshop.dto.ApiResponse;
import com.popularshop.dto.CouponResponse;
import com.popularshop.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<CouponResponse>> validateCoupon(@RequestBody java.util.Map<String, String> body) {
        String code = body.get("code");
        return ResponseEntity.ok(ApiResponse.success(couponService.validateCoupon(code)));
    }
}
