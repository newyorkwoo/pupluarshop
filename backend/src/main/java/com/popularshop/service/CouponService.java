package com.popularshop.service;

import com.popularshop.dto.*;
import com.popularshop.entity.Coupon;
import com.popularshop.exception.BadRequestException;
import com.popularshop.exception.ResourceNotFoundException;
import com.popularshop.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(CouponResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public CouponResponse validateCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "code", code));
        return CouponResponse.fromEntity(coupon);
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        if (couponRepository.existsByCode(request.getCode())) {
            throw new BadRequestException("優惠碼已存在");
        }
        Coupon coupon = Coupon.builder()
                .code(request.getCode().toUpperCase())
                .type(Coupon.CouponType.valueOf(request.getType()))
                .value(request.getValue())
                .minPurchase(request.getMinPurchase())
                .maxDiscount(request.getMaxDiscount())
                .maxUses(request.getMaxUses())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(request.isActive())
                .build();
        coupon = couponRepository.save(coupon);
        return CouponResponse.fromEntity(coupon);
    }

    @Transactional
    public CouponResponse updateCoupon(Long id, CouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "id", id));
        coupon.setCode(request.getCode().toUpperCase());
        coupon.setType(Coupon.CouponType.valueOf(request.getType()));
        coupon.setValue(request.getValue());
        coupon.setMinPurchase(request.getMinPurchase());
        coupon.setMaxDiscount(request.getMaxDiscount());
        coupon.setMaxUses(request.getMaxUses());
        coupon.setStartDate(request.getStartDate());
        coupon.setEndDate(request.getEndDate());
        coupon.setActive(request.isActive());
        coupon = couponRepository.save(coupon);
        return CouponResponse.fromEntity(coupon);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coupon", "id", id);
        }
        couponRepository.deleteById(id);
    }
}
