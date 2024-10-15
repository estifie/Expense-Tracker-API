package com.estifie.expensetracker.controller.v1;

import com.estifie.expensetracker.dto.user.UserGrantPermissionDTO;
import com.estifie.expensetracker.dto.user.UserRevokePermissionDTO;
import com.estifie.expensetracker.model.User;
import com.estifie.expensetracker.response.ApiResponse;
import com.estifie.expensetracker.response.user.UserPermissionsResponse;
import com.estifie.expensetracker.response.user.UsersResponse;
import com.estifie.expensetracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{username}/permissions")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSIONS') or hasAuthority('GRANT_PERMISSION')")
    public ResponseEntity<ApiResponse<Void>> grantPermission(@PathVariable String username, @RequestBody @Validated UserGrantPermissionDTO userGrantPermissionDTO) {
        userService.grantPermission(username, userGrantPermissionDTO.getPermission());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{username}/permissions")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSIONS') or hasAuthority('REVOKE_PERMISSION')")
    public ResponseEntity<ApiResponse<Void>> revokePermission(@PathVariable String username, @RequestBody @Validated UserRevokePermissionDTO userRevokePermissionDTO) {
        userService.revokePermission(username, userRevokePermissionDTO.getPermission());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{username}/permissions")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSIONS') or hasAuthority('VIEW_PERMISSIONS')")
    public ResponseEntity<ApiResponse<UserPermissionsResponse>> getPermissions(@PathVariable String username) {
        return ResponseEntity.ok(ApiResponse.<UserPermissionsResponse>success()
                .data(new UserPermissionsResponse(userService.getPermissions(username))));
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('MANAGE_USERS') or hasAuthority('VIEW_USERS')")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String username) {
        return ResponseEntity.ok(ApiResponse.<User>success()
                .data(userService.findByUsername(username)));
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('MANAGE_USERS') or hasAuthority('VIEW_USERS')")
    public ResponseEntity<ApiResponse<UsersResponse>> getUsers() {
        return ResponseEntity.ok(ApiResponse.<UsersResponse>success()
                .data(new UsersResponse(userService.findAll())));
    }

}
