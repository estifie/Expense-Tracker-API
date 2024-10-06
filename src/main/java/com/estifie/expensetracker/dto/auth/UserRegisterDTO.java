package com.estifie.expensetracker.dto.auth;


import com.estifie.expensetracker.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRegisterDTO {
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^(?!.*\\.\\.)(?!.*\\.$)\\w[\\w.]{0,29}$", message = "Username must be alphanumeric and at least 5 characters long")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 8 characters long")
    private String password;

    public UserRegisterDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toUser() {
        return new User(username, password);
    }
}
