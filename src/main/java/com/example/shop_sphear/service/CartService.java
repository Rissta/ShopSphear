package com.example.shop_sphear.service;

import com.example.shop_sphear.entity.shop_sphear.CartItem;
import com.example.shop_sphear.entity.shop_sphear.Products;
import com.example.shop_sphear.entity.shop_sphear.User;
import com.example.shop_sphear.exception.CartItemNotFoundException;
import com.example.shop_sphear.repository.shop_sphear.CartItemRepository;
import com.example.shop_sphear.repository.shop_sphear.ProductsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CurrentUserService currentUserService;
    private final ProductsRepository productsRepository;

    public CartService(CartItemRepository cartItemRepository, CurrentUserService currentUserService, ProductsRepository productsRepository) {
        this.cartItemRepository = cartItemRepository;
        this.currentUserService = currentUserService;
        this.productsRepository = productsRepository;
    }

    public List<CartItem> getAll(){
        User user = currentUserService.getCurrentUser();
        return cartItemRepository.findByUser(user);
    }
    @Transactional
    public void deleteById(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));;
        cartItemRepository.delete(cartItem);
    }

    public void addProductById(Long productId){
        User user = currentUserService.getCurrentUser();
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден" + productId));
        Optional<CartItem> existingCartItem  = cartItemRepository.findByUserAndProduct(user,product);
        if (existingCartItem.isPresent()){
            increaseQuantityById(existingCartItem.get().getId());
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setUser(user);
            newCartItem.setQuantity(1);
            cartItemRepository.save(newCartItem);
        }
    }

    @Transactional
    public void buyAll(){
        User user = currentUserService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Корзина пуста");
        }
        cartItemRepository.deleteByUser(user);
        reduceProductStock(cartItems);
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

    public void increaseQuantityById(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        Products product = cartItem.getProduct();
        if (cartItem.getQuantity()+1 <= product.getStock()){
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemRepository.save(cartItem);
        } else {
            throw new RuntimeException("Недостаточно товара: " + product.getName());
        }
    }

    public void decreaseQuantityById(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }
    }

}
