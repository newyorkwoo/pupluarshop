package com.popularshop.dto;

import com.popularshop.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private Long id;
    private String code;
    private String type;
    private BigDecimal value;
    private BigDecimal minPurchase;
    private BigDecimal maxDiscount;
    private Integer maxUses;
    private int usedCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private LocalDateTime createdAt;

    public static CouponResponse fromEntity(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .type(coupon.getType().name())
                .value(coupon.getValue())
                .minPurchase(coupon.getMinPurchase())
                .maxDiscount(coupon.getMaxDiscount())
                .maxUses(coupon.getMaxUses())
                .usedCount(coupon.getUsedCount())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .active(coupon.isActive())
                .createdAt(coupon.getCreatedAt())
                .build();
    }
}
