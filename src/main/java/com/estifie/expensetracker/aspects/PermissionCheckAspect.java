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
    @Before("@annotation(requiresPermission)")
    public void checkPermission(RequiresPermission requiresPermission) {
        if (!checkSinglePermission(requiresPermission.value().name())) {
            throw new AuthorizationException("Permission denied.");
        }
    }

    @Before("@annotation(requiresAnyPermission)")
    public void checkAnyPermission(RequiresAnyPermission requiresAnyPermission) {
        boolean hasPermission = Arrays.stream(requiresAnyPermission.value())
                .anyMatch(permission -> checkSinglePermission(permission.name()));

        if (!hasPermission) {
            throw new AuthorizationException("Permission denied.");
        }
    }

    @Before("@annotation(requiresAllPermissions)")
    public void checkAllPermissions(RequiresAllPermissions requiresAllPermissions) {
        boolean hasPermission = Arrays.stream(requiresAllPermissions.value())
                .allMatch(permission -> checkSinglePermission(permission.name()));

        if (!hasPermission) {
            throw new AuthorizationException("Permission denied.");
        }
    }

    private boolean checkSinglePermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(permission));
    }
}
