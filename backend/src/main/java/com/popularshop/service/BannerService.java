package com.popularshop.service;

import com.popularshop.dto.*;
import com.popularshop.entity.Banner;
import com.popularshop.exception.ResourceNotFoundException;
import com.popularshop.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    public List<BannerResponse> getActiveBanners() {
        return bannerRepository.findByActiveTrueOrderBySortOrder().stream()
                .map(BannerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BannerResponse> getAllBanners() {
        return bannerRepository.findAll().stream()
                .map(BannerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BannerResponse createBanner(BannerRequest request) {
        Banner banner = Banner.builder()
                .title(request.getTitle())
                .subtitle(request.getSubtitle())
                .imageUrl(request.getImageUrl())
                .link(request.getLink())
                .buttonText(request.getButtonText())
                .position(request.getPosition())
                .sortOrder(request.getSortOrder())
                .active(request.isActive())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        banner = bannerRepository.save(banner);
        return BannerResponse.fromEntity(banner);
    }

    @Transactional
    public BannerResponse updateBanner(Long id, BannerRequest request) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner", "id", id));
        banner.setTitle(request.getTitle());
        banner.setSubtitle(request.getSubtitle());
        banner.setImageUrl(request.getImageUrl());
        banner.setLink(request.getLink());
        banner.setButtonText(request.getButtonText());
        banner.setPosition(request.getPosition());
        banner.setSortOrder(request.getSortOrder());
        banner.setActive(request.isActive());
        banner.setStartDate(request.getStartDate());
        banner.setEndDate(request.getEndDate());
        banner = bannerRepository.save(banner);
        return BannerResponse.fromEntity(banner);
    }

    @Transactional
    public void deleteBanner(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Banner", "id", id);
        }
        bannerRepository.deleteById(id);
    }
}
