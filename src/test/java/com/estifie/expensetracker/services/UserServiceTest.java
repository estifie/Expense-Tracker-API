package com.estifie.expensetracker.services;

import com.estifie.expensetracker.exception.user.UserNotFoundException;
import com.estifie.expensetracker.model.User;
import com.estifie.expensetracker.repository.UserRepository;
import com.estifie.expensetracker.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername_UserExists() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        User result = userService.findByUsername("Alice");

        // Assert
        assertEquals("Alice", result.getUsername());
        verify(userRepository, times(1)).findByUsername("Alice");
    }

    @Test
    void testFindByUsername_UserNotExists() {
        // Arrange
        when(userRepository.findByUsername("Bob")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findByUsername("Bob"));
        verify(userRepository, times(1)).findByUsername("Bob");
    }

    @Test
    void testFindAll() {
        // Arrange
        User user1 = new User();
        user1.setUsername("Alice");
        User user2 = new User();
        user2.setUsername("Bob");
        user2.setDeactivatedAt(LocalDateTime.now());
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .anyMatch(user -> user.getUsername()
                        .equals("Alice")));
        assertTrue(result.stream()
                .anyMatch(user -> user.getUsername()
                        .equals("Bob")));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindAllActive() {
        // Arrange
        User user1 = new User();
        user1.setUsername("Alice");
        User user2 = new User();
        user2.setUsername("Bob");
        user2.setDeactivatedAt(LocalDateTime.now());
        when(userRepository.findAllActive()).thenReturn(List.of(user1));

        // Act
        List<User> result = userService.findAllActive();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.stream()
                .anyMatch(user -> user.getUsername()
                        .equals("Alice")));
        assertFalse(result.stream()
                .anyMatch(user -> user.getUsername()
                        .equals("Bob")));
        verify(userRepository, times(1)).findAllActive();
    }

    @Test
    void testFindAllDeleted() {
        // Arrange
        User user1 = new User();
        user1.setUsername("Alice");
        User user2 = new User();
        user2.setUsername("Bob");
        user2.setDeactivatedAt(LocalDateTime.now());
        when(userRepository.findAllDeleted()).thenReturn(List.of(user2));

        // Act
        List<User> result = userService.findAllDeleted();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.stream()
                .anyMatch(user -> user.getUsername()
                        .equals("Bob")));
        assertFalse(result.stream()
                .anyMatch(user -> user.getUsername()
                        .equals("Alice")));
        verify(userRepository, times(1)).findAllDeleted();
    }

    @Test
    void testCountAll() {
        // Arrange
        when(userRepository.countAll()).thenReturn(2L);

        // Act
        long result = userService.countAll();

        // Assert
        assertEquals(2, result);
        verify(userRepository, times(1)).countAll();
    }

    @Test
    void testCountActive() {
        // Arrange
        when(userRepository.countActive()).thenReturn(1L);

        // Act
        long result = userService.countActive();

        // Assert
        assertEquals(1, result);
        verify(userRepository, times(1)).countActive();
    }

    @Test
    void testCountDeleted() {
        // Arrange
        when(userRepository.countDeleted()).thenReturn(1L);

        // Act
        long result = userService.countDeleted();

        // Assert
        assertEquals(1, result);
        verify(userRepository, times(1)).countDeleted();
    }

    @Test
    void testDeactivateUser() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));
        when(userRepository.findAllDeactivated()).thenReturn(Collections.singletonList(user));

        // Act
        userService.deactivate("Alice");

        // Assert
        assertNotNull(user.getDeactivatedAt());
        assertTrue(userService.findAllDeactivated()
                .contains(user));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testActivateUser() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        user.setDeactivatedAt(LocalDateTime.now());
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        userService.activate("Alice");

        // Assert
        assertNull(user.getDeactivatedAt());
        assertFalse(userService.findAllDeactivated()
                .contains(user));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));
        when(userRepository.findAllDeleted()).thenReturn(Collections.singletonList(user));


        // Act
        userService.delete("Alice");

        // Assert
        assertNotNull(user.getDeletedAt());
        assertTrue(userService.findAllDeleted()
                .contains(user));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRestoreUser() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        user.setDeletedAt(LocalDateTime.now());
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        userService.restore("Alice");

        // Assert
        assertNull(user.getDeletedAt());
        assertFalse(userService.findAllDeleted()
                .contains(user));
        assertDoesNotThrow(() -> userService.findByUsername("Alice"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testIsUserDeleted_DeletedUser() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        user.setDeletedAt(LocalDateTime.now());
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.isUserDeleted("Alice");

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsUserDeleted_NonDeletedUser() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.isUserDeleted("Alice");

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsUserActive_ActiveUser() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.isUserActive("Alice");

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsUserActive_DeactiveUser() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        user.setDeactivatedAt(LocalDateTime.now());
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.isUserActive("Alice");

        // Assert
        assertFalse(result);
    }

    @Test
    void testGrantPermission() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        user.setPermissions(new HashSet<>());
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        userService.grantPermission("Alice", "TEST_PERMISSION");

        // Assert
        assertTrue(user.getPermissions()
                .contains("TEST_PERMISSION"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRevokePermission() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        user.setPermissions(new HashSet<>());
        user.addPermission("TEST_PERMISSION");
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        userService.revokePermission("Alice", "TEST_PERMISSION");

        // Assert
        assertFalse(user.getPermissions()
                .contains("TEST_PERMISSION"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetPermissions() {
        // Arrange
        User user = new User();
        user.setUsername("Alice");
        user.setPermissions(new HashSet<>());
        user.addPermission("TEST_PERMISSION");
        when(userRepository.findByUsername("Alice")).thenReturn(Optional.of(user));

        // Act
        Set<String> result = userService.getPermissions("Alice");

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains("TEST_PERMISSION"));
    }
}
