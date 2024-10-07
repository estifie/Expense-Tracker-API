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
    long countAll();
    long countActive();
    long countDeleted();
    long countDeactivated();
    void deactivate(String username);
    void activate(String username);
    void delete(String username);
    void restore(String username);
    boolean isUserDeleted(String username);
    boolean isUserActive(String username);
    void grantPermission(String username, String permissionName);
    void revokePermission(String username, String permissionName);
    Set<String> getPermissions(String username);
}
