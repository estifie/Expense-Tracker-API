package com.estifie.expensetracker.controller.v1;

import com.estifie.expensetracker.dto.auth.UserLoginDTO;
import com.estifie.expensetracker.dto.auth.UserRegisterDTO;
import com.estifie.expensetracker.response.ApiResponse;
import com.estifie.expensetracker.response.auth.UserLoginResponse;
import com.estifie.expensetracker.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        authenticationService.register(userRegisterDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String token = authenticationService.login(userLoginDTO);

        return ResponseEntity.ok(ApiResponse.<UserLoginResponse>success()
                .data(new UserLoginResponse(token)));
    }
}
