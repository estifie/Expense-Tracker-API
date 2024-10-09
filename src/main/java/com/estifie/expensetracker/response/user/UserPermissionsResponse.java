package com.estifie.expensetracker.response.user;

import java.util.Set;

public class UserPermissionsResponse {
    public Set<String> permissions;
    
    public UserPermissionsResponse(Set<String> permissions) {
        this.permissions = permissions;
    }
}
