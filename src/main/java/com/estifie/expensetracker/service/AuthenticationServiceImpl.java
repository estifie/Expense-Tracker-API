package com.estifie.expensetracker.service;

import com.estifie.expensetracker.dto.auth.UserLoginDTO;
import com.estifie.expensetracker.dto.auth.UserRegisterDTO;
import com.estifie.expensetracker.exception.global.BaseException;
import com.estifie.expensetracker.exception.user.UserNotFoundException;
import com.estifie.expensetracker.exception.validation.InvalidCredentialsException;
import com.estifie.expensetracker.exception.validation.MissingCredentialsException;
import com.estifie.expensetracker.exception.validation.UsernameAlreadyExistsException;
import com.estifie.expensetracker.model.User;
import com.estifie.expensetracker.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtServiceImpl;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtServiceImpl jwtServiceImpl, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtServiceImpl = jwtServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null || userRegisterDTO.getUsername() == null || userRegisterDTO.getPassword() == null) {
            throw new MissingCredentialsException("Username and password must be provided");
        }

        try {
            User user = new User();
            user.setUsername(userRegisterDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException();
        } catch (Exception e) {
            throw new BaseException("An error occurred while registering user");
        }
    }

    public String login(UserLoginDTO userLoginDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword()));

        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        } catch (Exception e) {
            throw new BaseException("An error occurred while authenticating user");
        }
        User user = userRepository.findByUsername(userLoginDTO.getUsername())
                .orElseThrow(UserNotFoundException::new);

        return jwtServiceImpl.generateToken(user);
    }
}
