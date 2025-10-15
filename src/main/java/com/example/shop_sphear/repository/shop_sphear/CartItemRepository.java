package com.example.shop_sphear.repository.shop_sphear;

import com.example.shop_sphear.entity.shop_sphear.CartItem;
import com.example.shop_sphear.entity.shop_sphear.Products;
import com.example.shop_sphear.entity.shop_sphear.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    List<CartItem> findByProduct(Products product);
    Optional<CartItem> findByUserAndProduct(User user, Products product);
    Integer countByUser(User user);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.id = :id")
    void updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Modifying
    void deleteByUser(User user);
    void deleteByUserAndProduct(User user, Products product);

    void deleteByUserAndProductId(User user, Long productId);
    @Query("SELECT SUM(c.quantity) FROM CartItem c WHERE c.user = :user")
    Integer sumQuantityByUser(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.product IN :products")
    void deleteAllByProducts(@Param("products") List<Products> products);
}
