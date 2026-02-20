package com.popularshop.dto;

import com.popularshop.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long productId;
    private Long userId;
    private String userName;
    private int rating;
    private String content;
    private String status;
    private LocalDateTime createdAt;

    public static ReviewResponse fromEntity(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .userId(review.getUser().getId())
                .userName(review.getUser().getName())
                .rating(review.getRating())
                .content(review.getContent())
                .status(review.getStatus().name())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
