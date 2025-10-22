package com.example.shop_sphear.controller;

import com.example.shop_sphear.entity.shop_sphear.ProductCategory;
import com.example.shop_sphear.entity.shop_sphear.Products;
import com.example.shop_sphear.repository.shop_sphear.CartItemRepository;
import com.example.shop_sphear.repository.shop_sphear.ProductCategoryRepository;
import com.example.shop_sphear.repository.shop_sphear.ProductsRepository;
import com.example.shop_sphear.service.CartService;
import com.example.shop_sphear.service.CurrentUserService;
import com.example.shop_sphear.service.ProductCategoryService;
import com.example.shop_sphear.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class EditProductController {

    private final ProductService productService;
    private final ProductCategoryService categoryService;

    public EditProductController(ProductService productService, ProductCategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/edit-product")
    public String editProductForm(@RequestParam(value = "id", required = false) Long id,
                                  Model model) {
        Optional<Products> product = productService.findById(id);

        if (product.isEmpty()) {
            return "redirect:/catalog";
        }

        List<ProductCategory> categories = categoryService.findAll();

        model.addAttribute("product", product.get());
        model.addAttribute("productCategories", categories);

        return "edit-product";
    }

    @PostMapping("/edit-product")
    public String updateProduct(@RequestParam(value = "id", required = true) Long id,
                                @Valid @ModelAttribute("product") Products product,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            List<ProductCategory> categories = categoryService.findAll();
            model.addAttribute("productCategories", categories);
            return "edit-product";
        }

        try {
            productService.updateProduct(id, product);
            return "redirect:/catalog";

        } catch (Exception e) {
            List<ProductCategory> categories = categoryService.findAll();
            model.addAttribute("productCategories", categories);
            model.addAttribute("error", "Ошибка при обновлении товара: " + e.getMessage());
            return "edit-product";
        }
    }
}
