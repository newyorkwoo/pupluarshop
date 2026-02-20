package com.popularshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank(message = "收件人不能為空")
    private String name;

    @NotBlank(message = "電話不能為空")
    private String phone;

    @NotBlank(message = "城市不能為空")
    private String city;

    @NotBlank(message = "區域不能為空")
    private String district;

    @NotBlank(message = "郵遞區號不能為空")
    private String zipCode;

    @NotBlank(message = "地址不能為空")
    private String addressLine;

    @JsonProperty("isDefault")
    private boolean isDefault;
}
