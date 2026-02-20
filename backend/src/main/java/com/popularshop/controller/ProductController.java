package com.popularshop.controller;

import com.popularshop.dto.*;
import com.popularshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProducts(page, size, sort, category, keyword)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductResponse>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductBySlug(slug)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id)));
    }

    @GetMapping("/new-arrivals")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getNewArrivals(
            @RequestParam(defaultValue = "8") int limit) {
        return ResponseEntity.ok(ApiResponse.success(productService.getNewArrivals(limit)));
    }

    @GetMapping("/on-sale")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getOnSale(
            @RequestParam(defaultValue = "8") int limit) {
        return ResponseEntity.ok(ApiResponse.success(productService.getOnSale(limit)));
    }
}
