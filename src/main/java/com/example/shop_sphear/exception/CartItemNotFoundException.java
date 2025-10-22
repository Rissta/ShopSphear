package com.example.shop_sphear.exception;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(Long cartItemId) {
        super("Cart item not found with id: " + cartItemId);
    }

}
