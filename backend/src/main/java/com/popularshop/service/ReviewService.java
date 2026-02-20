package com.popularshop.service;

import com.popularshop.dto.ReviewRequest;
import com.popularshop.dto.ReviewResponse;
import com.popularshop.dto.PageResponse;
import com.popularshop.entity.Product;
import com.popularshop.entity.Review;
import com.popularshop.entity.User;
import com.popularshop.exception.BadRequestException;
import com.popularshop.exception.ResourceNotFoundException;
import com.popularshop.repository.ProductRepository;
import com.popularshop.repository.ReviewRepository;
import com.popularshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getProductReviews(Long productId, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviews = reviewRepository.findByProductIdAndStatus(productId, Review.ReviewStatus.APPROVED,
                pageable);
        List<ReviewResponse> content = reviews.getContent().stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
        return PageResponse.of(content, reviews.getNumber(), reviews.getSize(),
                reviews.getTotalElements(), reviews.getTotalPages(), reviews.isLast());
    }

    @Transactional
    public ReviewResponse createReview(Long userId, ReviewRequest request) {
        if (reviewRepository.existsByUserIdAndProductId(userId, request.getProductId())) {
            throw new BadRequestException("您已經評論過此商品");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .content(request.getContent())
                .status(Review.ReviewStatus.PENDING)
                .build();
        review = reviewRepository.save(review);
        return ReviewResponse.fromEntity(review);
    }

    // Admin
    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getAllReviews(int page, int size, String status) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviews;
        if (status != null && !status.isBlank()) {
            reviews = reviewRepository.findByStatus(Review.ReviewStatus.valueOf(status), pageable);
        } else {
            reviews = reviewRepository.findAll(pageable);
        }
        List<ReviewResponse> content = reviews.getContent().stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
        return PageResponse.of(content, reviews.getNumber(), reviews.getSize(),
                reviews.getTotalElements(), reviews.getTotalPages(), reviews.isLast());
    }

    @Transactional
    public ReviewResponse updateReviewStatus(Long reviewId, String status) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        review.setStatus(Review.ReviewStatus.valueOf(status));
        reviewRepository.save(review);
        return ReviewResponse.fromEntity(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review", "id", reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }
}
