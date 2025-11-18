package com.electricity.backend.dto;

public class AuthResponse {

    private String token;
    private String accountNumber;
    private String username;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token, String accountNumber, String username) {
        this.token = token;
        this.accountNumber = accountNumber;
        this.username = username;
    }

    public AuthResponse(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}