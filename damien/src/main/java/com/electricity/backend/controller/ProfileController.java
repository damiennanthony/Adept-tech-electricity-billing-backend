package com.electricity.backend.controller;

import com.electricity.backend.entity.User;
import com.electricity.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profile", description = "User profile endpoints")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final UserRepository userRepository;

    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Get user profile", description = "Get current user's profile information")
    public ResponseEntity<ProfileResponse> getUserProfile() {
        try {
            String accountNumber = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ProfileResponse profile = new ProfileResponse();
            profile.setAccountNumber(user.getAccountNumber());
            profile.setUsername(user.getUserDisplayName());
            profile.setRole(user.getRole().name());
            profile.setCreatedAt(user.getCreatedAt());

            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public static class ProfileResponse {
        private String accountNumber;
        private String username;
        private String role;
        private java.time.LocalDateTime createdAt;

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public java.time.LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(java.time.LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}