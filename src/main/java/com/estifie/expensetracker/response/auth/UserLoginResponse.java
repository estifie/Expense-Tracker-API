package com.estifie.expensetracker.response.auth;

public class UserLoginResponse {
    public String token;

    public UserLoginResponse(String token) {
        this.token = token;
    }
}
