package com.estifie.expensetracker.controller.v1;

import com.estifie.expensetracker.annotations.RequiresAnyPermission;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.response.ApiResponse;
import com.estifie.expensetracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{username}/permissions")
    @RequiresAnyPermission({Permission.MANAGE_PERMISSIONS, Permission.GRANT_PERMISSION})
    public ResponseEntity<ApiResponse<Void>> grantPermission(String username, String permissionName) {
        userService.grantPermission(username, permissionName);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{username}/permissions")
    @RequiresAnyPermission({Permission.MANAGE_PERMISSIONS, Permission.REVOKE_PERMISSION})
    public ResponseEntity<ApiResponse<Void>> revokePermission(String username, String permissionName) {
        userService.revokePermission(username, permissionName);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{username}/permissions")
    @RequiresAnyPermission({Permission.MANAGE_PERMISSIONS, Permission.VIEW_PERMISSIONS})
    public ResponseEntity<ApiResponse<Void>> getPermissions(String username) {
        userService.getPermissions(username);
        return ResponseEntity.ok(ApiResponse.success());
    }

}
