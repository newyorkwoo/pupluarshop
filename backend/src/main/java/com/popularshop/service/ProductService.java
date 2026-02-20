package com.popularshop.service;

import com.popularshop.dto.*;
import com.popularshop.entity.*;
import com.popularshop.exception.BadRequestException;
import com.popularshop.exception.ResourceNotFoundException;
import com.popularshop.repository.CategoryRepository;
import com.popularshop.repository.ProductRepository;
import com.popularshop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProducts(int page, int size, String sort, String category, String keyword) {
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<Product> products;

        if (keyword != null && !keyword.isBlank()) {
            products = productRepository.search(keyword.trim(), pageable);
        } else if (category != null && !category.isBlank()) {
            Category cat = categoryRepository.findBySlug(category).orElse(null);
            if (cat != null) {
                List<Long> categoryIds = new java.util.ArrayList<>();
                categoryIds.add(cat.getId());
                if (cat.getChildren() != null && !cat.getChildren().isEmpty()) {
                    cat.getChildren().forEach(child -> categoryIds.add(child.getId()));
                }
                if (categoryIds.size() == 1) {
                    products = productRepository.findByCategoryIdAndActiveTrue(cat.getId(), pageable);
                } else {
                    products = productRepository.findByCategoryIdInAndActiveTrue(categoryIds, pageable);
                }
            } else {
                products = productRepository.findByActiveTrue(pageable);
            }
        } else {
            products = productRepository.findByActiveTrue(pageable);
        }

        List<ProductResponse> content = products.getContent().stream()
                .map(this::toResponseWithRating)
                .collect(Collectors.toList());

        return PageResponse.of(content, products.getNumber(), products.getSize(),
                products.getTotalElements(), products.getTotalPages(), products.isLast());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
        return toResponseWithRating(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return toResponseWithRating(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getNewArrivals(int limit) {
        return productRepository.findNewArrivals(PageRequest.of(0, limit)).stream()
                .map(this::toResponseWithRating)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getOnSale(int limit) {
        return productRepository.findOnSale(PageRequest.of(0, limit)).stream()
                .map(this::toResponseWithRating)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .slug(toSlug(request.getName()))
                .brand(request.getBrand())
                .description(request.getDescription())
                .material(request.getMaterial())
                .price(request.getPrice())
                .salePrice(request.getSalePrice())
                .stock(request.getStock())
                .active(request.isActive())
                .build();

        if (request.getCategoryId() != null) {
            Category cat = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(cat);
        }

        product = productRepository.save(product);

        if (request.getImages() != null) {
            Product finalProduct = product;
            request.getImages().forEach(img -> {
                ProductImage pi = ProductImage.builder()
                        .imageUrl(img.getImageUrl())
                        .sortOrder(img.getSortOrder())
                        .product(finalProduct)
                        .build();
                finalProduct.getImages().add(pi);
            });
        }
        if (request.getSizes() != null) {
            Product finalProduct = product;
            request.getSizes().forEach(s -> {
                ProductSize ps = ProductSize.builder()
                        .size(s.getSize())
                        .stock(s.getStock())
                        .product(finalProduct)
                        .build();
                finalProduct.getSizes().add(ps);
            });
        }

        product = productRepository.save(product);
        return ProductResponse.fromEntity(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setMaterial(request.getMaterial());
        product.setPrice(request.getPrice());
        product.setSalePrice(request.getSalePrice());
        product.setStock(request.getStock());
        product.setActive(request.isActive());

        if (request.getCategoryId() != null) {
            Category cat = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(cat);
        }

        // Update images
        if (request.getImages() != null) {
            product.getImages().clear();
            request.getImages().forEach(img -> {
                ProductImage pi = ProductImage.builder()
                        .imageUrl(img.getImageUrl())
                        .sortOrder(img.getSortOrder())
                        .product(product)
                        .build();
                product.getImages().add(pi);
            });
        }

        // Update sizes
        if (request.getSizes() != null) {
            product.getSizes().clear();
            request.getSizes().forEach(s -> {
                ProductSize ps = ProductSize.builder()
                        .size(s.getSize())
                        .stock(s.getStock())
                        .product(product)
                        .build();
                product.getSizes().add(ps);
            });
        }

        productRepository.save(product);
        return ProductResponse.fromEntity(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductResponse toggleProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setActive(!product.isActive());
        productRepository.save(product);
        return ProductResponse.fromEntity(product);
    }

    private ProductResponse toResponseWithRating(Product product) {
        ProductResponse resp = ProductResponse.fromEntity(product);
        try {
            resp.setAverageRating(reviewRepository.getAverageRatingByProductId(product.getId()));
            resp.setReviewCount(
                    reviewRepository.countByProductIdAndStatus(product.getId(), Review.ReviewStatus.APPROVED));
        } catch (Exception e) {
            resp.setAverageRating(0);
            resp.setReviewCount(0);
        }
        return resp;
    }

    private Sort parseSort(String sort) {
        if (sort == null)
            return Sort.by(Sort.Direction.DESC, "createdAt");
        return switch (sort) {
            case "price_asc" -> Sort.by(Sort.Direction.ASC, "price");
            case "price_desc" -> Sort.by(Sort.Direction.DESC, "price");
            case "name_asc" -> Sort.by(Sort.Direction.ASC, "name");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }

    private String toSlug(String input) {
        String nosep = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nosep, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}", "-").replaceAll("^-|-$", "");
    }
}
