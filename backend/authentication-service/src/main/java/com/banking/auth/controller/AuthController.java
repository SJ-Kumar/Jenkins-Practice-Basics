package com.banking.auth.controller;

import com.banking.auth.model.User;
import com.banking.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ✅ Register a new user
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists!";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        return "User registered successfully!";
    }

    // ✅ Login - Checks authentication
    @GetMapping
    public String authenticateUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return "Authenticated as: " + userDetails.getUsername();
        }
        return "User not authenticated";
    }
    
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserDetails(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Map<String, String> response = Map.of(
                "username", user.get().getUsername(),
                "password", user.get().getPassword(), // Already encoded
                "role", user.get().getRole()
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

}
