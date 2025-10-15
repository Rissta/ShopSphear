package com.example.shop_sphear.until;


//import com.github.javafaker.Faker;
import com.example.shop_sphear.entity.shop_sphear.ProductCategory;
import com.example.shop_sphear.entity.shop_sphear.Role;
import com.example.shop_sphear.entity.shop_sphear.User;
import com.example.shop_sphear.repository.shop_sphear.ProductCategoryRepository;
import com.example.shop_sphear.repository.shop_sphear.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public DataInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository, ProductCategoryRepository productCategoryRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @PostConstruct
    public void init() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(Role.ADMIN));
            userRepository.save(admin);
        }
        if (!userRepository.existsByUsername("user")) {
            User admin = new User();
            admin.setUsername("user");
            admin.setPassword(passwordEncoder.encode("user"));
            admin.setRoles(Set.of(Role.USER));
            userRepository.save(admin);
        }
    }

    @PostConstruct
    private void initCategories() {
        if (productCategoryRepository.count() == 0) {
            // Электроника
            createCategory("smartphones", "Смартфоны");
            createCategory("laptops", "Ноутбуки");
            createCategory("tablets", "Планшеты");
            createCategory("headphones", "Наушники");
            createCategory("smart-watches", "Умные часы");

            // Компьютерная техника
            createCategory("computers", "Компьютеры");
            createCategory("monitors", "Мониторы");
            createCategory("keyboards", "Клавиатуры");
            createCategory("mice", "Компьютерные мыши");

            // Бытовая техника
            createCategory("tv", "Телевизоры");
            createCategory("refrigerators", "Холодильники");
            createCategory("washing-machines", "Стиральные машины");
            createCategory("vacuum-cleaners", "Пылесосы");

            // Для дома
            createCategory("furniture", "Мебель");
            createCategory("lighting", "Освещение");
            createCategory("kitchen", "Кухонные принадлежности");

            // Одежда
            createCategory("mens-clothing", "Мужская одежда");
            createCategory("womens-clothing", "Женская одежда");
            createCategory("shoes", "Обувь");
            createCategory("accessories", "Аксессуары");

            // Спорт
            createCategory("fitness", "Фитнес");
            createCategory("outdoor", "Отдых на природе");
            createCategory("sports-equipment", "Спортивное оборудование");

            // Книги
            createCategory("fiction", "Художественная литература");
            createCategory("non-fiction", "Нехудожественная литература");
            createCategory("children-books", "Детские книги");

            System.out.println("Создано " + productCategoryRepository.count() + " категорий");
        }
    }


    private void createCategory(String slug, String name) {
        ProductCategory category = new ProductCategory();
        category.setName(name);
        category.setSlug(slug);
        productCategoryRepository.save(category);
    }
    
}
