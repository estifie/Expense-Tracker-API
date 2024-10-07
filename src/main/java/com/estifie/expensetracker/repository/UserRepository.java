package com.estifie.expensetracker.repository;

import com.estifie.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// deactivated can still be found but it is banned
// deleted cannot be found nor logged in

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.deletedAt IS NULL")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findByUsernameWithDeleted(String username);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.deactivatedAt IS NULL")
    List<User> findAllActive();

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NOT NULL")
    List<User> findAllDeleted();

    @Query("SELECT u FROM User u WHERE u.deactivatedAt IS NOT NULL")
    List<User> findAllDeactivated();

    @Query("SELECT COUNT(u) FROM User u")
    long countAll();

    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL AND u.deactivatedAt IS NULL")
    long countActive();

    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NOT NULL")
    long countDeleted();

    @Query("SELECT COUNT(u) FROM User u WHERE u.deactivatedAt IS NOT NULL")
    long countDeactivated();

}
