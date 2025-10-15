package com.example.shop_sphear.controller;

import com.example.shop_sphear.entity.shop_sphear.CartItem;
import com.example.shop_sphear.entity.shop_sphear.Products;
import com.example.shop_sphear.entity.shop_sphear.User;
import com.example.shop_sphear.repository.shop_sphear.CartItemRepository;
import com.example.shop_sphear.repository.shop_sphear.ProductCategoryRepository;
import com.example.shop_sphear.repository.shop_sphear.ProductsRepository;
import com.example.shop_sphear.service.CartService;
import com.example.shop_sphear.service.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class CatalogController {

    private final ProductsRepository productsRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CurrentUserService currentUserService;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    public CatalogController(ProductsRepository productsRepository, ProductCategoryRepository productCategoryRepository, CurrentUserService currentUserService, CartItemRepository cartItemRepository, CartService cartService) {
        this.productsRepository = productsRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.currentUserService = currentUserService;
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
    }

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "sort", defaultValue = "popular") String sort,
            @RequestParam(value = "minPrice", required = false) Float minPrice,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice,
            Model model) {
        List<Products> products = productsRepository.findAll();
        products.sort(Comparator.comparing(Products::getCreatedAt));
        model.addAttribute("products", products);
        return "catalog";
    }

    @PostMapping("/catalog/add-to-cart")
    public String addProduct(@RequestParam Long id){
            User user = currentUserService.getCurrentUser();
            Optional<Products> product = productsRepository.findById(id);
            if (product.isPresent()){
                cartService.addToCart(user, product.get());
            }
        return "redirect:/catalog";
    }
}
