package com.estifie.expensetracker.dto.user;

import jakarta.validation.constraints.NotBlank;

public class UserRevokePermissionDTO {
    @NotBlank
    private String permission;

    public UserRevokePermissionDTO() {
    }

    public UserRevokePermissionDTO(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
