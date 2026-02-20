package com.popularshop.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull(message = "商品ID不能為空")
    private Long productId;

    @Min(value = 1, message = "評分最低為1")
    @Max(value = 5, message = "評分最高為5")
    private int rating;

    private String content;
}
