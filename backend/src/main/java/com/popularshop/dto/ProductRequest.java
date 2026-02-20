package com.popularshop.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    @NotBlank(message = "商品名稱不能為空")
    private String name;

    @NotBlank(message = "品牌不能為空")
    private String brand;

    private String description;
    private String material;

    @NotNull(message = "價格不能為空")
    @DecimalMin(value = "0", message = "價格不能為負")
    private BigDecimal price;

    private BigDecimal salePrice;

    @Min(value = 0, message = "庫存不能為負")
    private int stock;

    private Long categoryId;
    private boolean active = true;

    private List<ProductImageRequest> images;
    private List<ProductSizeRequest> sizes;

    @Data
    public static class ProductImageRequest {
        private String imageUrl;
        private int sortOrder;
    }

    @Data
    public static class ProductSizeRequest {
        private String size;
        private int stock;
    }
}
