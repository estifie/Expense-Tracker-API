package com.estifie.expensetracker.service;

import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.exception.user.UserNotFoundException;
import com.estifie.expensetracker.model.User;
import com.estifie.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Component("userService")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElseThrow(UserNotFoundException::new);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllActive() {
        return userRepository.findAllActive();
    }

    public List<User> findAllDeleted() {
        return userRepository.findAllDeleted();
    }

    public List<User> findAllDeactivated() {
        return userRepository.findAllDeactivated();
    }

    public boolean hasPermission(String username, String permissionName) {
        User user = this.findByUsername(username);
        return user.getPermissions().contains(permissionName);
    }

    public long countAll() {
        return userRepository.countAll();
    }

    public long countActive() {
        return userRepository.countActive();
    }

    public long countDeleted() {
        return userRepository.countDeleted();
    }

    public long countDeactivated() {
        return userRepository.countDeactivated();
    }

    public void deactivate(String username) {
        User user = this.findByUsername(username);
        user.setDeactivatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void activate(String username) {
        User user = this.findByUsername(username);
        user.setDeactivatedAt(null);
        userRepository.save(user);
    }


    public void delete(String username, boolean hardDelete) {
        User user = this.findByUsername(username);

        user.setDeletedAt(LocalDateTime.now());

        boolean canHardDelete = this.hasPermission(username, Permission.HARD_DELETE_USER.name());

        if (canHardDelete && hardDelete) {
            userRepository.delete(user);
        } else {
            userRepository.save(user);
        }
    }

    public void restore(String username) {
        User user = this.findByUsername(username);
        user.setDeletedAt(null);
        userRepository.save(user);
    }

    public boolean isUserDeleted(String username) {
        User user = this.findByUsername(username);
        return user.getDeletedAt() != null;
    }

    public boolean isUserActive(String username) {
        User user = this.findByUsername(username);
        return user.getDeactivatedAt() == null && user.getDeletedAt() == null;
    }

    public void grantPermission(String username, String permissionName) {
        try {
            Permission.valueOf(permissionName);
            User user = this.findByUsername(username);
            user.getPermissions().add(permissionName);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid permission: " + permissionName);
        }
    }

    public void grantPermissionBulk(String username, Set<String> permissionNames) {
        Set<String> invalidPermissions = permissionNames.stream()
                .filter(permissionName -> {
                    try {
                        Permission.valueOf(permissionName);
                        return false;
                    } catch (IllegalArgumentException e) {
                        return true;
                    }
                })
                .collect(java.util.stream.Collectors.toSet());

        if (!invalidPermissions.isEmpty()) {
            throw new IllegalArgumentException("Invalid permissions: " + String.join(", ", invalidPermissions));
        }

        User user = this.findByUsername(username);
        user.getPermissions().addAll(permissionNames);
        userRepository.save(user);
    }

    public void revokePermission(String username, String permissionName) {
        User user = this.findByUsername(username);
        user.getPermissions().remove(permissionName);
        userRepository.save(user);
    }

    public Set<String> getPermissions(String username) {
        User user = this.findByUsername(username);
        return user.getPermissions();
    }
}
