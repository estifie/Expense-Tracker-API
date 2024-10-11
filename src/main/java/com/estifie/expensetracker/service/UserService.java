package com.estifie.expensetracker.service;

import com.estifie.expensetracker.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User findByUsername(String username);

    List<User> findAll();

    List<User> findAllActive();

    List<User> findAllDeleted();

    List<User> findAllDeactivated();

    boolean hasPermission(String username, String permissionName);

    long countAll();

    long countActive();

    long countDeleted();

    long countDeactivated();

    void deactivate(String username);

    void activate(String username);

    void delete(String username, boolean hardDelete);

    void restore(String username);

    boolean isUserDeleted(String username);

    boolean isUserActive(String username);

    void grantPermission(String username, String permissionName);

    void grantPermissionBulk(String username, Set<String> permissionNames);

    void revokePermission(String username, String permissionName);

    Set<String> getPermissions(String username);
}
