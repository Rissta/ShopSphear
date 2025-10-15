package com.example.shop_sphear.service;

import com.example.shop_sphear.entity.shop_sphear.User;
import com.example.shop_sphear.repository.shop_sphear.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }
        }
        throw new RuntimeException("User not authenticated");
    }

//    // Альтернативный метод с обработкой анонимного пользователя
//    public User getCurrentUserOrNull() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null &&
//                authentication.isAuthenticated() &&
//                !(authentication.getPrincipal() instanceof String)) {
//
//            Object principal = authentication.getPrincipal();
//            if (principal instanceof UserDetails) {
//                String username = ((UserDetails) principal).getUsername();
//                return userRepository.findByUsername(username).orElse(null);
//            }
//        }
//        return null;
//    }
}