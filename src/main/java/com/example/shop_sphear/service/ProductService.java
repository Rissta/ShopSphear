package com.example.shop_sphear.service;

import com.example.shop_sphear.entity.shop_sphear.Products;
import com.example.shop_sphear.repository.shop_sphear.ProductsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductsRepository productRepository;

    public ProductService(ProductsRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Products> findById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Page<Products> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<Products> findAll() {
        return productRepository.findAll();
    }

    public Page<Products> findFilteredProductsWithPagination(String name, Long categoryId, Float minPrice, Float maxPrice, String sort, int page, int size) {
        Pageable pageable = createPageable(page, size, sort);

        if (name != null && !name.isEmpty()) {
            if (categoryId != null) {
                if (minPrice != null && maxPrice != null) {
                    return productRepository.findByNameContainingIgnoreCaseAndCategoryIdAndPriceBetween(name, categoryId, minPrice, maxPrice, pageable);
                } else if (minPrice != null) {
                    return productRepository.findByNameContainingIgnoreCaseAndCategoryIdAndPriceGreaterThanEqual(name, categoryId, minPrice, pageable);
                } else if (maxPrice != null) {
                    return productRepository.findByNameContainingIgnoreCaseAndCategoryIdAndPriceLessThanEqual(name, categoryId, maxPrice, pageable);
                } else {
                    return productRepository.findByNameContainingIgnoreCaseAndCategoryId(name, categoryId, pageable);
                }
            } else {
                if (minPrice != null && maxPrice != null) {
                    return productRepository.findByNameContainingIgnoreCaseAndPriceBetween(name, minPrice, maxPrice, pageable);
                } else if (minPrice != null) {
                    return productRepository.findByNameContainingIgnoreCaseAndPriceGreaterThanEqual(name, minPrice, pageable);
                } else if (maxPrice != null) {
                    return productRepository.findByNameContainingIgnoreCaseAndPriceLessThanEqual(name, maxPrice, pageable);
                } else {
                    return productRepository.findByNameContainingIgnoreCase(name, pageable);
                }
            }
        } else {
            if (categoryId != null) {
                if (minPrice != null && maxPrice != null) {
                    return productRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice, pageable);
                } else if (minPrice != null) {
                    return productRepository.findByCategoryIdAndPriceGreaterThanEqual(categoryId, minPrice, pageable);
                } else if (maxPrice != null) {
                    return productRepository.findByCategoryIdAndPriceLessThanEqual(categoryId, maxPrice, pageable);
                } else {
                    return productRepository.findByCategoryId(categoryId, pageable);
                }
            } else {
                if (minPrice != null && maxPrice != null) {
                    return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
                } else if (minPrice != null) {
                    return productRepository.findByPriceGreaterThanEqual(minPrice, pageable);
                } else if (maxPrice != null) {
                    return productRepository.findByPriceLessThanEqual(maxPrice, pageable);
                } else {
                    return productRepository.findAll(pageable);
                }
            }
        }
    }

    private Pageable createPageable(int page, int size, String sort) {
        Sort sorting = createSort(sort);
        return PageRequest.of(page, size, sorting);
    }

    private Sort createSort(String sort) {
        switch (sort) {
            case "price_asc":
                return Sort.by(Sort.Direction.ASC, "price");
            case "price_desc":
                return Sort.by(Sort.Direction.DESC, "price");
            case "popular":
                return Sort.by(Sort.Direction.DESC, "countReviews");
            case "rating":
                return Sort.by(Sort.Direction.DESC, "reviews");
            case "newest":
            default:
                return Sort.by(Sort.Direction.DESC, "createdAt");
        }
    }

    public Products updateProduct(Long id, Products productDetails) {
        Products existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден с id: " + id));

        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setOldPrice(productDetails.getOldPrice());
        existingProduct.setImageUrl(productDetails.getImageUrl());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setStock(productDetails.getStock());
        existingProduct.setReviews(productDetails.getReviews());
        existingProduct.setCountReviews(productDetails.getCountReviews());

        return productRepository.save(existingProduct);
    }
}