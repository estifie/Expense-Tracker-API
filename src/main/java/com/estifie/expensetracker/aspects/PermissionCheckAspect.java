package com.estifie.expensetracker.aspects;

import com.estifie.expensetracker.annotations.RequiresAllPermissions;
import com.estifie.expensetracker.annotations.RequiresAnyPermission;
import com.estifie.expensetracker.annotations.RequiresPermission;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.exception.auth.AuthorizationException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class PermissionCheckAspect {
    @Before("@annotation(RequiresPermission)")
    public void checkPermission(RequiresPermission requiresPermission) {
        checkSinglePermission(requiresPermission.value().name());
    }

    @Before("@annotation(RequiresAnyPermission)")
    public void checkAnyPermission(RequiresAnyPermission requiresAnyPermission) {
        boolean hasPermission = Arrays.stream(requiresAnyPermission.value())
                .anyMatch(permission -> checkSinglePermission(permission.name()));

        if (!hasPermission) {
            throw new AuthorizationException("At least one permission is required.");
        }
    }

    @Before("@annotation(requiresAllPermissions)")
    public void checkAllPermissions(RequiresAllPermissions requiresAllPermissions) {
        Arrays.stream(requiresAllPermissions.value())
                .forEach(permission -> checkSinglePermission(permission.name()));
    }

    private boolean checkSinglePermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthorizationException("User is not authenticated.");
        }

        if (!authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(permission))) {
            throw new AuthorizationException("User does not have required permission: " + permission);
        }

        return true;
    }
}
