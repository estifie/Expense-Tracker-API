package com.estifie.expensetracker.service;

import com.estifie.expensetracker.model.User;

import java.util.Set;

public interface UserService {
    User findByUsername(String username);

    void grantPermission(String username, String permissionName);
    void revokePermission(String username, String permissionName);
    Set<String> getPermissions(String username);
}
