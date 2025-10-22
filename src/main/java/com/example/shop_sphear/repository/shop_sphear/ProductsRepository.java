package com.example.shop_sphear.repository.shop_sphear;

import com.example.shop_sphear.entity.shop_sphear.ProductCategory;
import com.example.shop_sphear.entity.shop_sphear.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Поиск по названию
    Page<Products> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Поиск по категории
    Page<Products> findByCategoryId(Long categoryId, Pageable pageable);

    // Поиск по цене (диапазон)
    Page<Products> findByPriceBetween(Float minPrice, Float maxPrice, Pageable pageable);

    // Поиск по минимальной цене
    Page<Products> findByPriceGreaterThanEqual(Float minPrice, Pageable pageable);

    // Поиск по максимальной цене
    Page<Products> findByPriceLessThanEqual(Float maxPrice, Pageable pageable);

    // Комбинированные запросы: название + категория
    Page<Products> findByNameContainingIgnoreCaseAndCategoryId(String name, Long categoryId, Pageable pageable);

    // Комбинированные запросы: название + цена (диапазон)
    Page<Products> findByNameContainingIgnoreCaseAndPriceBetween(String name, Float minPrice, Float maxPrice, Pageable pageable);

    // Комбинированные запросы: название + минимальная цена
    Page<Products> findByNameContainingIgnoreCaseAndPriceGreaterThanEqual(String name, Float minPrice, Pageable pageable);

    // Комбинированные запросы: название + максимальная цена
    Page<Products> findByNameContainingIgnoreCaseAndPriceLessThanEqual(String name, Float maxPrice, Pageable pageable);

    // Комбинированные запросы: категория + цена (диапазон)
    Page<Products> findByCategoryIdAndPriceBetween(Long categoryId, Float minPrice, Float maxPrice, Pageable pageable);

    // Комбинированные запросы: категория + минимальная цена
    Page<Products> findByCategoryIdAndPriceGreaterThanEqual(Long categoryId, Float minPrice, Pageable pageable);

    // Комбинированные запросы: категория + максимальная цена
    Page<Products> findByCategoryIdAndPriceLessThanEqual(Long categoryId, Float maxPrice, Pageable pageable);

    // Комбинированные запросы: название + категория + цена (диапазон)
    Page<Products> findByNameContainingIgnoreCaseAndCategoryIdAndPriceBetween(String name, Long categoryId, Float minPrice, Float maxPrice, Pageable pageable);

    // Комбинированные запросы: название + категория + минимальная цена
    Page<Products> findByNameContainingIgnoreCaseAndCategoryIdAndPriceGreaterThanEqual(String name, Long categoryId, Float minPrice, Pageable pageable);

    // Комбинированные запросы: название + категория + максимальная цена
    Page<Products> findByNameContainingIgnoreCaseAndCategoryIdAndPriceLessThanEqual(String name, Long categoryId, Float maxPrice, Pageable pageable);

    @Query("SELECT MAX(p.price) FROM Products p")
    Float findMaxPrice();
}
