package com.example.shop_sphear.controller;

import com.example.shop_sphear.entity.shop_sphear.Role;
import com.example.shop_sphear.entity.shop_sphear.User;
import com.example.shop_sphear.repository.shop_sphear.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Set;

@Controller
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegistrationController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String showRegistrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public  String createAccount(@RequestParam String username,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 Model model){
        Optional<User> userByUserName = userRepository.findByUsername(username);
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if(userByEmail.isPresent() || userByUserName.isPresent()){
            model.addAttribute("error", "Пользователь с таким именем или почтой существует");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "registration";
        }
        if(!confirmPassword.equals(password)){
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "registration";
        }
        // Проверяем длину пароля
//        if(password.length() < 8) {
//            model.addAttribute("error", "Пароль должен содержать не менее 8 символов");
//            model.addAttribute("username", username);
//            model.addAttribute("email", email);
//            return "registration";
//        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user);
        return "redirect:/login?registered";
    }
}
