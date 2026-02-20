package com.popularshop.controller;

import com.popularshop.dto.ApiResponse;
import com.popularshop.dto.BannerResponse;
import com.popularshop.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerResponse>>> getActiveBanners() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getActiveBanners()));
    }
}
