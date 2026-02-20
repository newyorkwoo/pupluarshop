package com.popularshop.repository;

import com.popularshop.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProductIdAndStatus(Long productId, Review.ReviewStatus status, Pageable pageable);

    Page<Review> findByUserId(Long userId, Pageable pageable);

    Page<Review> findByStatus(Review.ReviewStatus status, Pageable pageable);

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.product.id = :productId AND r.status = 'APPROVED'")
    double getAverageRatingByProductId(Long productId);

    long countByProductIdAndStatus(Long productId, Review.ReviewStatus status);
}
