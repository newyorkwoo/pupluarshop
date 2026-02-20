package com.popularshop.dto;

import com.popularshop.entity.Product;
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
public class ProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String brand;
    private String description;
    private String material;
    private BigDecimal price;
    private BigDecimal salePrice;
    private int stock;
    private boolean active;
    private Long categoryId;
    private String categoryName;
    private List<ProductImageDto> images;
    private List<ProductSizeDto> sizes;
    private double averageRating;
    private long reviewCount;
    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImageDto {
        private Long id;
        private String imageUrl;
        private int sortOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSizeDto {
        private Long id;
        private String size;
        private int stock;
    }

    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .brand(product.getBrand())
                .description(product.getDescription())
                .material(product.getMaterial())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .stock(product.getStock())
                .active(product.isActive())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .images(product.getImages() != null ? product.getImages().stream()
                        .map(img -> ProductImageDto.builder()
                                .id(img.getId())
                                .imageUrl(img.getImageUrl())
                                .sortOrder(img.getSortOrder())
                                .build())
                        .collect(Collectors.toList()) : List.of())
                .sizes(product.getSizes() != null ? product.getSizes().stream()
                        .map(s -> ProductSizeDto.builder()
                                .id(s.getId())
                                .size(s.getSize())
                                .stock(s.getStock())
                                .build())
                        .collect(Collectors.toList()) : List.of())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
