package com.example.shop_sphear.controller;

import com.example.shop_sphear.entity.shop_sphear.CartItem;
import com.example.shop_sphear.entity.shop_sphear.Products;
import com.example.shop_sphear.entity.shop_sphear.User;
import com.example.shop_sphear.repository.shop_sphear.CartItemRepository;
import com.example.shop_sphear.repository.shop_sphear.ProductsRepository;
import com.example.shop_sphear.service.CartService;
import com.example.shop_sphear.service.CurrentUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CartController {

    private final CartService cartService;
    private final ProductsRepository productsRepository;
    private final CartItemRepository cartItemRepository;
    private final CurrentUserService currentUserService;

    public CartController(CartService cartService, ProductsRepository productsRepository, CartItemRepository cartItemRepository, CurrentUserService currentUserService) {
        this.cartService = cartService;
        this.productsRepository = productsRepository;
        this.cartItemRepository = cartItemRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/cart")
    public String Cart(Model model){
        User user = currentUserService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        cartItems.sort(Comparator.comparing(CartItem::getCreatedAt));

        Integer countItem = cartItems.stream()
                .map(cartItem -> cartItem.getQuantity())
                .mapToInt(Integer::intValue)
                .sum();
        Integer totalPrice = cartItems.stream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getProduct().getPrice())
                .mapToInt(Float::intValue)
                .sum();
        Integer savings = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getOldPrice() > cartItem.getProduct().getPrice())
                .map(cartItem -> (cartItem.getProduct().getOldPrice() - cartItem.getProduct().getPrice()) * cartItem.getQuantity())
                .mapToInt(Float::intValue)
                .sum();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("count",countItem);
        model.addAttribute("savings",savings);
        model.addAttribute("totalPrice",totalPrice);
        return "cart";
    }

    @PostMapping("/cart/delete-by-id")
    public String cartDeleteById(@RequestParam Long productId){
        User user = currentUserService.getCurrentUser();
        Optional<Products> product = productsRepository.findById(productId);
        List<CartItem> cartItem = cartItemRepository.findByProduct(product.get());
        cartItemRepository.delete(cartItem.getFirst());
        return "redirect:/cart";
    }

    @PostMapping("/cart/go-to-buy")
    @Transactional
    public String cartDeleteAll() {
        User user = currentUserService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteByUser(user);
        reduceProductStock(cartItems);
        return "redirect:/cart";
    }

    @Transactional
    private void reduceProductStock(List<CartItem> cartItems) {
        List<Products> productsToDelete = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Products product = cartItem.getProduct();
            int newStock = product.getStock() - cartItem.getQuantity();

            if (newStock > 0) {
                product.setStock(newStock);
                productsRepository.save(product);
            } else if (newStock == 0) {
                product.setStock(0);
                productsRepository.save(product);
                productsToDelete.add(product);
            } else {
                throw new RuntimeException("Недостаточно товара: " + product.getName());
            }
        }
        if (!productsToDelete.isEmpty()) {
            cartItemRepository.deleteAllByProducts(productsToDelete);
            productsRepository.deleteAll(productsToDelete);
        }
    }

    @PostMapping("/cart/increase-quantity")
    public String increaseQuantity(@RequestParam Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        Products product = cartItem.getProduct();
        if (cartItem.getQuantity()+1 <= product.getStock()){
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemRepository.save(cartItem);
        } else {
            throw new RuntimeException("Недостаточно товара: " + product.getName());
        }
        return "redirect:/cart";
    }
    @PostMapping("/cart/decrease-quantity")
    public String decreaseQuantity(@RequestParam Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        if (cartItem.getQuantity()-1 > 0){
            cartItem.setQuantity(cartItem.getQuantity()-1);
            cartItemRepository.save(cartItem);
        } else if (cartItem.getQuantity()-1 == 0) {
            cartItemRepository.delete(cartItem);
        }
        return "redirect:/cart";
    }

}
