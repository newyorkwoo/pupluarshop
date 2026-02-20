package com.popularshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponRequest {
    @NotBlank(message = "優惠碼不能為空")
    private String code;

    @NotBlank(message = "類型不能為空")
    private String type; // PERCENTAGE or FIXED

    private BigDecimal value;
    private BigDecimal minPurchase;
    private BigDecimal maxDiscount;
    private Integer maxUses;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active = true;
}
