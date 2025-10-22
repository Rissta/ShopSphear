package com.example.shop_sphear.service;

import com.example.shop_sphear.entity.shop_sphear.ProductCategory;
import com.example.shop_sphear.repository.shop_sphear.ProductCategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository categoryRepository;

    public ProductCategoryService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<ProductCategory> findAll() {
        return categoryRepository.findAll();
    }
}