package com.coffeeshop.backend.service;

import com.coffeeshop.backend.dto.CreateUserRequest;
import com.coffeeshop.backend.entity.Role;
import com.coffeeshop.backend.entity.User;
import com.coffeeshop.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));  // Chuyển chuỗi thành enum Role

        return userRepo.save(user);
    }

}
