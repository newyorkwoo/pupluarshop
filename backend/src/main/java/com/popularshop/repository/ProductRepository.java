package com.popularshop.repository;

import com.popularshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findBySlug(String slug);

    Page<Product> findByCategoryIdAndActiveTrue(Long categoryId, Pageable pageable);

    Page<Product> findByCategoryIdInAndActiveTrue(List<Long> categoryIds, Pageable pageable);

    Page<Product> findByActiveTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.active = true AND (LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR LOWER(p.brand) LIKE LOWER(CONCAT('%',:keyword,'%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%',:keyword,'%')))")
    Page<Product> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.createdAt DESC")
    List<Product> findNewArrivals(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.salePrice IS NOT NULL AND p.salePrice > 0 ORDER BY p.createdAt DESC")
    List<Product> findOnSale(Pageable pageable);

    List<Product> findByBrandAndActiveTrue(String brand);

    long countByActiveTrue();
}
