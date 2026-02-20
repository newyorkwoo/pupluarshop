package com.popularshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotNull(message = "商品ID不能為空")
    private Long productId;

    private String size;

    @Min(value = 1, message = "數量至少為1")
    private int quantity = 1;
}
