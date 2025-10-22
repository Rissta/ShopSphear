package com.example.shop_sphear.controller;

import com.example.shop_sphear.entity.shop_sphear.ProductCategory;
import com.example.shop_sphear.entity.shop_sphear.Products;
import com.example.shop_sphear.repository.shop_sphear.ProductCategoryRepository;
import com.example.shop_sphear.repository.shop_sphear.ProductsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class AddProductController {

    public final ProductsRepository productsRepository;
    public final ProductCategoryRepository productCategoryRepository;

    public AddProductController(ProductsRepository productsRepository,ProductCategoryRepository productCategoryRepository) {
        this.productsRepository = productsRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping("/add-product")
    public String Product(Model model) {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        model.addAttribute("productCategories", productCategories);
        return "add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam String name,
                             @RequestParam Float price,
                             @RequestParam Long category,
                             @RequestParam Integer stock,
                             @RequestParam String imageUrl,
                             @RequestParam String description) {

        ProductCategory productCategory = productCategoryRepository.findById(category)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category slug: " + category));

        Products products = new Products();
        products.setName(name);
        products.setPrice(price);
        products.setCategory(productCategory);
        products.setStock(stock);
        products.setImageUrl(imageUrl);
        products.setDescription(description);
        productsRepository.save(products);
        return  "redirect:/add-product";
    }
}
