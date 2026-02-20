package com.popularshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    @NotBlank(message = "收件人不能為空")
    private String shippingName;

    @NotBlank(message = "電話不能為空")
    private String shippingPhone;

    @NotBlank(message = "城市不能為空")
    private String shippingCity;

    private String shippingDistrict;

    private String shippingZipCode;

    @NotBlank(message = "地址不能為空")
    private String shippingAddress;

    @NotBlank(message = "付款方式不能為空")
    private String paymentMethod;

    private String couponCode;
    private String note;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private String size;
        private int quantity;
    }
}
