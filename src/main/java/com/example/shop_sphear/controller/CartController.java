package com.example.shop_sphear.controller;

import com.example.shop_sphear.entity.shop_sphear.CartItem;
import com.example.shop_sphear.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String getData(Model model){
        List<CartItem> cartItems = cartService.getAll();

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
    public String cartDeleteById(@RequestParam Long cartItemId){
        cartService.deleteById(cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/go-to-buy")
    public String buyProduct() {
        cartService.buyAll();
        return "redirect:/cart";
    }

    @PostMapping("/cart/increase-quantity")
    public String increaseQuantity(@RequestParam Long cartItemId,
                                   Model model){
        cartService.increaseQuantityById(cartItemId);
        return "redirect:/cart";
    }
    @PostMapping("/cart/decrease-quantity")
    public String decreaseQuantity(@RequestParam Long cartItemId,
                                   Model model){
        cartService.decreaseQuantityById(cartItemId);
        return "redirect:/cart";
    }

}
