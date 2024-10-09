package com.estifie.expensetracker.service;

import com.estifie.expensetracker.exception.user.UserNotFoundException;
import com.estifie.expensetracker.model.User;
import com.estifie.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
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

    public void delete(String username) {
        User user = this.findByUsername(username);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
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
        User user = this.findByUsername(username);
        user.getPermissions().add(permissionName);
        userRepository.save(user);
    }

    public void grantPermissionBulk(String username, Set<String> permissionNames) {
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
