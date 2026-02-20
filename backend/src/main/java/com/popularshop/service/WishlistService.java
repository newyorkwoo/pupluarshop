package com.popularshop.service;

import com.popularshop.entity.Product;
import com.popularshop.entity.User;
import com.popularshop.entity.Wishlist;
import com.popularshop.dto.ProductResponse;
import com.popularshop.exception.BadRequestException;
import com.popularshop.exception.ResourceNotFoundException;
import com.popularshop.repository.ProductRepository;
import com.popularshop.repository.UserRepository;
import com.popularshop.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(w -> ProductResponse.fromEntity(w.getProduct()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addToWishlist(Long userId, Long productId) {
        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new BadRequestException("商品已在願望清單中");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();
        wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Transactional(readOnly = true)
    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }
}
