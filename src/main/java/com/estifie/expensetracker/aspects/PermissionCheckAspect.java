package com.estifie.expensetracker.aspects;

import com.estifie.expensetracker.annotations.RequiresAllPermissions;
import com.estifie.expensetracker.annotations.RequiresAnyPermission;
import com.estifie.expensetracker.annotations.RequiresPermission;
import com.estifie.expensetracker.exception.auth.AuthorizationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@Aspect
@Component
public class PermissionCheckAspect {
    @Before("@annotation(requiresPermission)")
    public void checkPermission(JoinPoint joinPoint, RequiresPermission requiresPermission) {
        if (!checkSinglePermission(joinPoint, requiresPermission.value()
                .name())) {
            throw new AuthorizationException("Permission denied.");
        }
    }

    @Before("@annotation(requiresAnyPermission)")
    public void checkAnyPermission(JoinPoint joinPoint, RequiresAnyPermission requiresAnyPermission) {
        boolean hasPermission = Arrays.stream(requiresAnyPermission.value())
                .anyMatch(permission -> checkSinglePermission(joinPoint, permission.name()));

        if (!hasPermission) {
            throw new AuthorizationException("Permission denied.");
        }
    }

    @Before("@annotation(requiresAllPermissions)")
    public void checkAllPermissions(JoinPoint joinPoint, RequiresAllPermissions requiresAllPermissions) {
        boolean hasPermission = Arrays.stream(requiresAllPermissions.value())
                .allMatch(permission -> checkSinglePermission(joinPoint, permission.name()));

        if (!hasPermission) {
            throw new AuthorizationException("Permission denied.");
        }
    }

    private boolean checkSinglePermission(JoinPoint joinPoint, String permission) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (permission.equals("OWNERSHIP")) {
            Parameter[] parameters = ((MethodSignature) joinPoint.getSignature()).getMethod()
                    .getParameters();
            Object[] args = joinPoint.getArgs();

            return getUsernameFromParameters(parameters, args).filter(username -> authentication.getName()
                    .equals(username)).isPresent();
        }

        return authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(permission));
    }

    private Optional<String> getUsernameFromParameters(Parameter[] parameters, Object[] args) {
        return IntStream.range(0, parameters.length)
                .filter(i -> parameters[i].getName().equals("username"))
                .mapToObj(i -> args[i].toString())
                .findFirst();
    }
}
