package com.electricity.backend.service;

import com.electricity.backend.dto.AuthResponse;
import com.electricity.backend.dto.LoginRequest;
import com.electricity.backend.dto.RegisterRequest;
import com.electricity.backend.entity.User;
import com.electricity.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByAccountNumber(request.getAccountNumber())) {
            return new AuthResponse("Account number already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse("Username already exists");
        }

        var user = new User(
                request.getAccountNumber(),
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken, user.getAccountNumber(), user.getUserDisplayName());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getAccountNumber(),
                            request.getPassword()
                    )
            );

            var user = (User) authentication.getPrincipal();
            var jwtToken = jwtService.generateToken(user);

            return new AuthResponse(jwtToken, user.getAccountNumber(), user.getUserDisplayName());
        } catch (AuthenticationException e) {
            return new AuthResponse("Invalid account number or password");
        }
    }
}