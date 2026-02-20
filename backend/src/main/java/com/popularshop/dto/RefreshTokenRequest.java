package com.popularshop.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token 不能為空")
    private String refreshToken;
}
