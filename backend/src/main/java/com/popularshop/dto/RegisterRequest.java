package com.popularshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "姓名不能為空")
    @Size(min = 2, max = 50, message = "姓名長度需在 2~50 之間")
    private String name;

    @NotBlank(message = "Email 不能為空")
    @Email(message = "Email 格式不正確")
    private String email;

    @NotBlank(message = "密碼不能為空")
    @Size(min = 8, message = "密碼長度至少 8 位")
    private String password;

    private String phone;
}
