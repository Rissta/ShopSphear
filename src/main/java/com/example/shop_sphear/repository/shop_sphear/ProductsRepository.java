package com.example.shop_sphear.repository.shop_sphear;

import com.example.shop_sphear.entity.shop_sphear.ProductCategory;
import com.example.shop_sphear.entity.shop_sphear.Products;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    Optional<Products> findByName(String username);
    List<ProductCategory> findByCategory(ProductCategory category);
    List<Products> findByCategoryId(Long categoryId, Sort sort);

    List<Products> findByCategoryIdAndPriceBetween(Long categoryId, Float minPrice, Float maxPrice, Sort sort);

    List<Products> findByCategoryIdAndPriceGreaterThanEqual(Long categoryId, Float minPrice, Sort sort);

    List<Products> findByCategoryIdAndPriceLessThanEqual(Long categoryId, Float maxPrice, Sort sort);

    List<Products> findByPriceBetween(Float minPrice, Float maxPrice, Sort sort);

    List<Products> findByPriceGreaterThanEqual(Float minPrice, Sort sort);

    List<Products> findByPriceLessThanEqual(Float maxPrice, Sort sort);

    @Query("SELECT MAX(p.price) FROM Products p")
    Float findMaxPrice();
}
