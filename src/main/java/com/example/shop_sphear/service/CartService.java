package com.example.shop_sphear.service;

import com.example.shop_sphear.entity.shop_sphear.CartItem;
import com.example.shop_sphear.entity.shop_sphear.Products;
import com.example.shop_sphear.entity.shop_sphear.User;
import com.example.shop_sphear.repository.shop_sphear.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;

    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public void addToCart(User user, Products product) {
        addToCart(user, product, 1);
    }

    public void addToCart(User user, Products product, Integer quantity) {
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.increaseQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            cartItemRepository.save(newCartItem);
        }
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public Integer getCartItemsCount(User user) {
        return cartItemRepository.countByUser(user);
    }

    public void removeFromCart(User user, Products products) {
        cartItemRepository.deleteByUserAndProduct(user, products);
    }

    public void updateQuantity(User user, Products products, Integer quantity) {
        if (quantity <= 0) {
            removeFromCart(user, products);
        } else {
            cartItemRepository.findByUserAndProduct(user, products)
                    .ifPresent(cartItem -> {
                        cartItem.setQuantity(quantity);
                        cartItemRepository.save(cartItem);
                    });
        }
    }
}
