package com.estifie.expensetracker.controllers;

import com.estifie.expensetracker.aspects.PermissionCheckAspect;
import com.estifie.expensetracker.controller.v1.UserController;
import com.estifie.expensetracker.dto.user.UserGrantPermissionDTO;
import com.estifie.expensetracker.service.JwtServiceImpl;
import com.estifie.expensetracker.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@EnableAspectJAutoProxy
@AutoConfigureMockMvc
@Import(PermissionCheckAspect.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @MockBean
    private JwtServiceImpl jwtServiceImpl;

    @Test
    public void testGrantPermission_whenNotAuthenticated() throws Exception {
        // Arrange
        String username = "alice";
        String permissionName = "TEST_PERMISSION";
        UserGrantPermissionDTO userGrantPermissionDTO = new UserGrantPermissionDTO(permissionName);

        // Act
        mockMvc.perform(post("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{ \"permission\": \"" + permissionName + "\" }"))
                .andExpect(status().isUnauthorized());

        // Assert
        Mockito.verify(userServiceImpl, Mockito.never())
                .grantPermission(eq(username), eq(userGrantPermissionDTO.getPermission()));
    }

    @Test
    @WithMockUser(username = "alice")
    public void testGrantPermission_whenAuthenticatedButNoPermission() throws Exception {
        // Arrange
        String username = "alice";
        String permissionName = "TEST_PERMISSION";
        UserGrantPermissionDTO userGrantPermissionDTO = new UserGrantPermissionDTO(permissionName);

        // Act
        mockMvc.perform(post("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{ \"permission\": \"" + permissionName + "\" }"))
                .andExpect(status().isUnauthorized());

        // Assert
        Mockito.verify(userServiceImpl, Mockito.never())
                .grantPermission(eq(username), eq(userGrantPermissionDTO.getPermission()));
    }

    @Test
    @WithMockUser(username = "alice", authorities = "MANAGE_PERMISSIONS")
    public void testGrantPermission_whenAuthenticatedAndHasPermission_1() throws Exception {
        // Arrange
        String username = "alice";
        String permissionName = "TEST_PERMISSION";
        UserGrantPermissionDTO userGrantPermissionDTO = new UserGrantPermissionDTO(permissionName);

        // Act
        mockMvc.perform(post("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{ \"permission\": \"" + permissionName + "\" }"))
                .andExpect(status().isOk());

        // Assert
        Mockito.verify(userServiceImpl)
                .grantPermission(eq(username), eq(userGrantPermissionDTO.getPermission()));
    }

    @Test
    @WithMockUser(username = "alice", authorities = "GRANT_PERMISSION")
    public void testGrantPermission_whenAuthenticatedAndHasPermission_2() throws Exception {
        // Arrange
        String username = "alice";
        String permissionName = "TEST_PERMISSION";
        UserGrantPermissionDTO userGrantPermissionDTO = new UserGrantPermissionDTO(permissionName);

        // Act
        mockMvc.perform(post("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{ \"permission\": \"" + permissionName + "\" }"))
                .andExpect(status().isOk());

        // Assert
        Mockito.verify(userServiceImpl)
                .grantPermission(eq(username), eq(userGrantPermissionDTO.getPermission()));
    }

    @Test
    public void testRevokePermission_whenNotAuthenticated() throws Exception {
        // Arrange
        String username = "alice";
        String permissionName = "TEST_PERMISSION";
        UserGrantPermissionDTO userGrantPermissionDTO = new UserGrantPermissionDTO(permissionName);

        // Act
        mockMvc.perform(post("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{ \"permission\": \"" + permissionName + "\" }"))
                .andExpect(status().isUnauthorized());

        // Assert
        Mockito.verify(userServiceImpl, Mockito.never())
                .revokePermission(eq(username), eq(userGrantPermissionDTO.getPermission()));
    }

    @Test
    @WithMockUser(username = "alice")
    public void testRevokePermission_whenAuthenticatedButNoPermission() throws Exception {
        // Arrange
        String username = "alice";
        String permissionName = "TEST_PERMISSION";
        UserGrantPermissionDTO userGrantPermissionDTO = new UserGrantPermissionDTO(permissionName);

        // Act
        mockMvc.perform(post("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{ \"permission\": \"" + permissionName + "\" }"))
                .andExpect(status().isUnauthorized());

        // Assert
        Mockito.verify(userServiceImpl, Mockito.never())
                .revokePermission(eq(username), eq(userGrantPermissionDTO.getPermission()));
    }

    @Test
    @WithMockUser(username = "alice", authorities = "MANAGE_PERMISSIONS")
    public void testRevokePermission_whenAuthenticatedAndHasPermission_1() throws Exception {
        // Arrange
        String username = "alice";
        String permissionName = "TEST_PERMISSION";
        UserGrantPermissionDTO userGrantPermissionDTO = new UserGrantPermissionDTO(permissionName);

        // Act
        mockMvc.perform(delete("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{ \"permission\": \"" + permissionName + "\" }"))
                .andExpect(status().isOk());

        // Assert
        Mockito.verify(userServiceImpl)
                .revokePermission(eq(username), eq(userGrantPermissionDTO.getPermission()));
    }

    @Test
    @WithMockUser(username = "alice", authorities = "REVOKE_PERMISSION")
    public void testRevokePermission_whenAuthenticatedAndHasPermission_2() throws Exception {
        // Arrange
        String username = "alice";
        String permissionName = "TEST_PERMISSION";
        UserGrantPermissionDTO userGrantPermissionDTO = new UserGrantPermissionDTO(permissionName);

        // Act
        mockMvc.perform(delete("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{ \"permission\": \"" + permissionName + "\" }"))
                .andExpect(status().isOk());

        // Assert
        Mockito.verify(userServiceImpl)
                .revokePermission(eq(username), eq(userGrantPermissionDTO.getPermission()));
    }

    @Test
    public void testGetPermissions_whenNotAuthenticated() throws Exception {
        // Arrange
        String username = "alice";

        // Act
        mockMvc.perform(post("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        // Assert
        Mockito.verify(userServiceImpl, Mockito.never())
                .getPermissions(eq(username));
    }

    @Test
    @WithMockUser(username = "alice")
    public void testGetPermissions_whenAuthenticatedButNoPermission() throws Exception {
        // Arrange
        String username = "alice";

        // Act
        mockMvc.perform(get("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        // Assert
        Mockito.verify(userServiceImpl, Mockito.never())
                .getPermissions(eq(username));
    }

    @Test
    @WithMockUser(username = "alice", authorities = "MANAGE_PERMISSIONS")
    public void testGetPermissions_whenAuthenticatedAndHasPermission_1() throws Exception {
        // Arrange
        String username = "alice";

        // Act
        mockMvc.perform(get("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        // Assert
        Mockito.verify(userServiceImpl)
                .getPermissions(eq(username));
    }

    @Test
    @WithMockUser(username = "alice", authorities = "VIEW_PERMISSIONS")
    public void testGetPermissions_whenAuthenticatedAndHasPermission_2() throws Exception {
        // Arrange
        String username = "alice";

        // Act
        mockMvc.perform(get("/v1/users/" + username + "/permissions").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        // Assert
        Mockito.verify(userServiceImpl)
                .getPermissions(eq(username));
    }
}
