package com.estifie.expensetracker.dto.user;

import jakarta.validation.constraints.NotBlank;

public class UserGrantPermissionDTO {
    @NotBlank(message = "Permission name is required")
    private String permission;

    public UserGrantPermissionDTO() {
    }

    public UserGrantPermissionDTO(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
