package com.banking.smartbank.auth.service;

import com.banking.smartbank.auth.model.User;
import com.banking.smartbank.auth.repository.UserRepository;
import com.banking.smartbank.auth.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil = new JwtUtil();

    public AuthService(UserRepository ur, PasswordEncoder pe) {
        this.userRepo = ur; this.encoder = pe;
    }

    public String register(String name, String email, String password) {
        if (userRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setName(name); user.setEmail(email);
        user.setPassword(encoder.encode(password));
        userRepo.save(user);
        return "User registered successfully";
    }

    public String login(String email, String password) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtil.generateToken(email);
    }
}
