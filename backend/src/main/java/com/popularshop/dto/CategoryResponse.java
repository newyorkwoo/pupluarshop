package com.popularshop.dto;

import com.popularshop.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private Long parentId;
    private int sortOrder;
    private List<CategoryResponse> children;

    public static CategoryResponse fromEntity(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .sortOrder(category.getSortOrder())
                .children(category.getChildren() != null
                        ? category.getChildren().stream().map(CategoryResponse::fromEntity).collect(Collectors.toList())
                        : List.of())
                .build();
    }
}
