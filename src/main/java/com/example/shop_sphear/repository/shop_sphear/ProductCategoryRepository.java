package com.example.shop_sphear.repository.shop_sphear;

import com.example.shop_sphear.entity.shop_sphear.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findBySlug(String slug);

}
