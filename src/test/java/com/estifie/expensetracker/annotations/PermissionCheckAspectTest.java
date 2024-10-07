package com.estifie.expensetracker.annotations;

import com.estifie.expensetracker.aspects.PermissionCheckAspect;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.exception.auth.AuthorizationException;
import com.estifie.expensetracker.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PermissionCheckAspectTest {
    @Autowired
    private PermissionCheckAspect permissionCheckAspect;

    @MockBean
    private UserServiceImpl userService;

    @Test
    @WithMockUser(username = "alice")
    public void testCheckPermission_withoutPermission() {
        RequiresPermission requiresPermission = mock(RequiresPermission.class);
        when(requiresPermission.value()).thenReturn(Permission.MANAGE_PERMISSIONS);

        assertThrows(AuthorizationException.class, () -> permissionCheckAspect.checkPermission(requiresPermission));
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"DIFFERENT_PERMISSION"})
    public void testCheckPermission_withDifferentPermission() {
        RequiresPermission requiresPermission = mock(RequiresPermission.class);
        when(requiresPermission.value()).thenReturn(Permission.MANAGE_PERMISSIONS);

        assertThrows(AuthorizationException.class, () -> permissionCheckAspect.checkPermission(requiresPermission));
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"MANAGE_PERMISSIONS"})
    public void testCheckPermission_withPermission() {
        RequiresPermission requiresPermission = mock(RequiresPermission.class);
        when(requiresPermission.value()).thenReturn(Permission.MANAGE_PERMISSIONS);

        permissionCheckAspect.checkPermission(requiresPermission);
    }

    @Test
    @WithMockUser(username = "alice")
    public void testCheckAnyPermission_withoutPermission() {
        RequiresAnyPermission requiresAnyPermission = mock(RequiresAnyPermission.class);
        when(requiresAnyPermission.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        assertThrows(AuthorizationException.class, () -> permissionCheckAspect.checkAnyPermission(requiresAnyPermission));
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"DIFFERENT_PERMISSION"})
    public void testCheckAnyPermission_withDifferentPermission() {
        RequiresAnyPermission requiresAnyPermission = mock(RequiresAnyPermission.class);
        when(requiresAnyPermission.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        assertThrows(AuthorizationException.class, () -> permissionCheckAspect.checkAnyPermission(requiresAnyPermission));
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"MANAGE_PERMISSIONS"})
    public void testCheckAnyPermission_withOnePermission_1() {
        RequiresAnyPermission requiresAnyPermission = mock(RequiresAnyPermission.class);
        when(requiresAnyPermission.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        permissionCheckAspect.checkAnyPermission(requiresAnyPermission);
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"GRANT_PERMISSION"})
    public void testCheckAnyPermission_withOnePermission_2() {
        RequiresAnyPermission requiresAnyPermission = mock(RequiresAnyPermission.class);
        when(requiresAnyPermission.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        permissionCheckAspect.checkAnyPermission(requiresAnyPermission);
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"MANAGE_PERMISSIONS", "GRANT_PERMISSION"})
    public void testCheckAnyPermission_withMultiplePermissions() {
        RequiresAnyPermission requiresAnyPermission = mock(RequiresAnyPermission.class);
        when(requiresAnyPermission.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        permissionCheckAspect.checkAnyPermission(requiresAnyPermission);
    }

    @Test
    @WithMockUser(username = "alice")
    public void testCheckAllPermissions_withoutPermission() {
        RequiresAllPermissions requiresAllPermissions = mock(RequiresAllPermissions.class);
        when(requiresAllPermissions.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        assertThrows(AuthorizationException.class, () -> permissionCheckAspect.checkAllPermissions(requiresAllPermissions));
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"MANAGE_PERMISSIONS"})
    public void testCheckAllPermissions_withOnePermission() {
        RequiresAllPermissions requiresAllPermissions = mock(RequiresAllPermissions.class);
        when(requiresAllPermissions.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        assertThrows(AuthorizationException.class, () -> permissionCheckAspect.checkAllPermissions(requiresAllPermissions));
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"MANAGE_PERMISSIONS", "DIFFERENT_PERMISSION"})
    public void testCheckAllPermissions_withDifferentPermission() {
        RequiresAllPermissions requiresAllPermissions = mock(RequiresAllPermissions.class);
        when(requiresAllPermissions.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        assertThrows(AuthorizationException.class, () -> permissionCheckAspect.checkAllPermissions(requiresAllPermissions));
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"MANAGE_PERMISSIONS", "GRANT_PERMISSION"})
    public void testCheckAllPermissions_withAllPermissions() {
        RequiresAllPermissions requiresAllPermissions = mock(RequiresAllPermissions.class);
        when(requiresAllPermissions.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        permissionCheckAspect.checkAllPermissions(requiresAllPermissions);
    }

    @Test
    @WithMockUser(username = "alice", authorities = {"MANAGE_PERMISSIONS", "GRANT_PERMISSION", "DIFFERENT_PERMISSION"})
    public void testCheckAllPermissions_withAllPermissionsAndDifferentPermission() {
        RequiresAllPermissions requiresAllPermissions = mock(RequiresAllPermissions.class);
        when(requiresAllPermissions.value()).thenReturn(new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION});

        permissionCheckAspect.checkAllPermissions(requiresAllPermissions);
    }
}
