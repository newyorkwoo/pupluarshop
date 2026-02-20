package com.popularshop.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "目前密碼不能為空")
    private String currentPassword;

    @NotBlank(message = "新密碼不能為空")
    @Size(min = 8, message = "新密碼長度至少 8 位")
    private String newPassword;
}
