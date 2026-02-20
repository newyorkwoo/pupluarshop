package com.popularshop.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UpdateProfileRequest {
    @Size(min = 2, max = 50, message = "姓名長度需在 2~50 之間")
    private String name;
    private String phone;
}
