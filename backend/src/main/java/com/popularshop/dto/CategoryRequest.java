package com.popularshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "分類名稱不能為空")
    private String name;
    private String description;
    private String imageUrl;
    private Long parentId;
    private int sortOrder;
}
