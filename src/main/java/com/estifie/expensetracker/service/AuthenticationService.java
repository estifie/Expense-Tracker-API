package com.estifie.expensetracker.service;

import com.estifie.expensetracker.dto.auth.UserLoginDTO;
import com.estifie.expensetracker.dto.auth.UserRegisterDTO;

public interface AuthenticationService {
    void register(UserRegisterDTO userRegisterDTO);
    String login(UserLoginDTO userLoginDTO);
}
