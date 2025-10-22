package com.example.shop_sphear.controller;

import com.example.shop_sphear.entity.shop_sphear.CartItem;
import com.example.shop_sphear.entity.shop_sphear.ProductCategory;
import com.example.shop_sphear.entity.shop_sphear.Products;
import com.example.shop_sphear.entity.shop_sphear.User;
import com.example.shop_sphear.repository.shop_sphear.CartItemRepository;
import com.example.shop_sphear.repository.shop_sphear.ProductCategoryRepository;
import com.example.shop_sphear.repository.shop_sphear.ProductsRepository;
import com.example.shop_sphear.service.CartService;
import com.example.shop_sphear.service.CurrentUserService;
import com.example.shop_sphear.service.ProductCategoryService;
import com.example.shop_sphear.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    public CatalogController(ProductsRepository productsRepository, ProductCategoryRepository productCategoryRepository, CurrentUserService currentUserService, CartItemRepository cartItemRepository, CartService cartService, ProductService productService, ProductCategoryService productCategoryService) {
        this.productsRepository = productsRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.currentUserService = currentUserService;
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "sort", defaultValue = "newest") String sort,
            @RequestParam(value = "minPrice", required = false) Float minPrice,
            @RequestParam(value = "maxPrice", required = false) Float maxPrice,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            Model model) {

        // Получаем отфильтрованные продукты с пагинацией
        Page<Products> productPage = productService.findFilteredProductsWithPagination(
                name, categoryId, minPrice, maxPrice, sort, page, size);

        List<ProductCategory> categories = productCategoryService.findAll();

        // Рассчитываем диапазон страниц для отображения (максимум 5 кнопок)
        int totalPages = productPage.getTotalPages();
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, startPage + 4);

        // Корректируем startPage, если endPage достиг максимума
        if (endPage - startPage < 4 && startPage > 0) {
            startPage = Math.max(0, endPage - 4);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productCategories", categories);
        model.addAttribute("currentName", name);
        model.addAttribute("currentCategory", categoryId);
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentMinPrice", minPrice);
        model.addAttribute("currentMaxPrice", maxPrice);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalProducts", productPage.getTotalElements());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "catalog";
    }

    @GetMapping("/catalog-no-filter")
    public String catalogNoFilter(Model model){
        List<Products> products = productService.findAll();
        model.addAttribute("products", products);
        return "catalog";
    }

    @PostMapping("/catalog/add-to-cart")
    public String addProduct(@RequestParam Long id){
        cartService.addProductById(id);
        return "redirect:/catalog";
    }

    @GetMapping("/catalog/delete")
    public String updateProduct(@RequestParam(value = "id", required = true) Long id) {
        productService.deleteById(id);
        return "redirect:/catalog";
    }
}
