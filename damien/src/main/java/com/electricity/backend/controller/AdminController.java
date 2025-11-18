package com.electricity.backend.controller;

import com.electricity.backend.dto.UsageRecordRequest;
import com.electricity.backend.dto.UsageRecordResponse;
import com.electricity.backend.entity.User;
import com.electricity.backend.service.UsageService;
import com.electricity.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin endpoints for managing usage data")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UsageService usageService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UsageService usageService, UserRepository userRepository) {
        this.usageService = usageService;
        this.userRepository = userRepository;
    }

    @PostMapping("/usage")
    @Operation(summary = "Add usage record for any user", description = "Admin endpoint to add usage record for specified user")
    public ResponseEntity<UsageRecordResponse> addUsageRecord(@Valid @RequestBody UsageRecordRequest request) {
        try {
            if (request.getAccountNumber() == null || request.getAccountNumber().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            UsageRecordResponse response = usageService.addUsageRecordForUser(
                    request.getAccountNumber(), request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Admin endpoint to get all registered users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserResponse> userResponses = users.stream()
                    .map(this::mapToUserResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userResponses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/users/{accountNumber}")
    @Operation(summary = "Get user by account number", description = "Admin endpoint to get user details by account number")
    public ResponseEntity<UserResponse> getUserByAccountNumber(@PathVariable String accountNumber) {
        try {
            User user = userRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(mapToUserResponse(user));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setAccountNumber(user.getAccountNumber());
        response.setUsername(user.getUserDisplayName());
        response.setRole(user.getRole().name());
        response.setCreatedAt(user.getCreatedAt());
        response.setEnabled(user.isEnabled());
        return response;
    }

    public static class UserResponse {
        private Long id;
        private String accountNumber;
        private String username;
        private String role;
        private java.time.LocalDateTime createdAt;
        private boolean enabled;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

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

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}