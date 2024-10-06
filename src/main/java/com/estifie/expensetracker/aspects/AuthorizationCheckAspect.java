package com.estifie.expensetracker.aspects;

import com.estifie.expensetracker.annotations.RequiresAuthorization;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.exception.auth.AuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Aspect for handling authorization checks based on the {@link RequiresAuthorization} annotation.
 * This aspect ensures that the authenticated user is either the same as the user specified in the request
 * or has the required permission to perform the action.
 */
@Aspect
@Component
public class AuthorizationCheckAspect {
    @Before("@annotation(RequiresAuthorization)")
    public void checkAuthorization(RequiresAuthorization requiresAuthorization) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String usernameFromRequest = request.getParameter("username");
        if (usernameFromRequest == null) {
            throw new AuthorizationException("Username is required in the request");
        }

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthorizationException();
        }

        String authenticatedUsername = authentication.getName();
        if (authenticatedUsername.equals(usernameFromRequest)) {
            return;
        }

        Permission requiredPermission = requiresAuthorization.value();
        if (authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(requiredPermission.name()))) {
            return;
        }

        throw new AuthorizationException();
    }
}
