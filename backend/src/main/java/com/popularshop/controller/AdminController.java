package com.popularshop.controller;

import com.popularshop.dto.*;
import com.popularshop.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final UserService userService;
    private final CouponService couponService;
    private final BannerService bannerService;
    private final ReviewService reviewService;

    // Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getDashboard()));
    }

    // Products
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProducts(page, size, null, null, keyword)));
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success("商品已建立", productService.createProduct(request)));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success("商品已更新", productService.updateProduct(id, request)));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("商品已刪除", null));
    }

    @PatchMapping("/products/{id}/toggle")
    public ResponseEntity<ApiResponse<ProductResponse>> toggleProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("商品狀態已更新", productService.toggleProduct(id)));
    }

    // Categories
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAllCategories()));
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("分類已建立", categoryService.createCategory(request)));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("分類已更新", categoryService.updateCategory(id, request)));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("分類已刪除", null));
    }

    // Orders
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getAllOrders(page, size, status)));
    }

    @GetMapping("/orders/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderByNumber(orderNumber)));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(@PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity
                .ok(ApiResponse.success("訂單狀態已更新", orderService.updateOrderStatus(id, body.get("status"))));
    }

    // Users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(page, size)));
    }

    @PutMapping("/users/{id}/toggle-active")
    public ResponseEntity<ApiResponse<Void>> toggleUser(@PathVariable Long id) {
        userService.toggleUserActive(id);
        return ResponseEntity.ok(ApiResponse.success("使用者狀態已更新", null));
    }

    // Coupons
    @GetMapping("/coupons")
    public ResponseEntity<ApiResponse<List<CouponResponse>>> getCoupons() {
        return ResponseEntity.ok(ApiResponse.success(couponService.getAllCoupons()));
    }

    @PostMapping("/coupons")
    public ResponseEntity<ApiResponse<CouponResponse>> createCoupon(@Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(ApiResponse.success("優惠券已建立", couponService.createCoupon(request)));
    }

    @PutMapping("/coupons/{id}")
    public ResponseEntity<ApiResponse<CouponResponse>> updateCoupon(@PathVariable Long id,
            @Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(ApiResponse.success("優惠券已更新", couponService.updateCoupon(id, request)));
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(ApiResponse.success("優惠券已刪除", null));
    }

    // Banners
    @GetMapping("/banners")
    public ResponseEntity<ApiResponse<List<BannerResponse>>> getBanners() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAllBanners()));
    }

    @PostMapping("/banners")
    public ResponseEntity<ApiResponse<BannerResponse>> createBanner(@Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok(ApiResponse.success("橫幅已建立", bannerService.createBanner(request)));
    }

    @PutMapping("/banners/{id}")
    public ResponseEntity<ApiResponse<BannerResponse>> updateBanner(@PathVariable Long id,
            @Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok(ApiResponse.success("橫幅已更新", bannerService.updateBanner(id, request)));
    }

    @DeleteMapping("/banners/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok(ApiResponse.success("橫幅已刪除", null));
    }

    // Reviews
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponse>>> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAllReviews(page, size, status)));
    }

    @PutMapping("/reviews/{id}/status")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReviewStatus(@PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("評論狀態已更新", reviewService.updateReviewStatus(id, status)));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success("評論已刪除", null));
    }
}
