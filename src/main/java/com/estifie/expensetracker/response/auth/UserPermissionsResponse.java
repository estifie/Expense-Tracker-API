package com.estifie.expensetracker.response.auth;

import java.util.Set;

public class UserPermissionsResponse {
    public Set<String> permissions;

    public UserPermissionsResponse(Set<String> permissions) {
        this.permissions = permissions;
    }
}
