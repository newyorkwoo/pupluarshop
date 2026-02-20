package com.popularshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BannerRequest {
    @NotBlank(message = "標題不能為空")
    private String title;
    private String subtitle;
    @NotBlank(message = "圖片不能為空")
    private String imageUrl;
    private String link;
    private String buttonText;
    private String position;
    private int sortOrder;
    private boolean active = true;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
