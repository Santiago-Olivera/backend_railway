package com.BackendChallenge.TechTrendEmporium.service;

import com.BackendChallenge.TechTrendEmporium.Auth.*;
import com.BackendChallenge.TechTrendEmporium.JWT.JwtService;
import com.BackendChallenge.TechTrendEmporium.entity.User;
import com.BackendChallenge.TechTrendEmporium.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.UnknownServiceException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;


    @Test
    public void loginTest() {
        LoginRequest request = new LoginRequest();
        request.setPassword("testAuth1");
        AuthResponse authResponse = new AuthResponse();
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(authResponse, HttpStatus.OK);
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setLogged(false);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        ResponseEntity<Object> response = authService.login(request);
        assertEquals(responseEntity, response);
        user.setLogged(true);
        ResponseEntity<Object> response2 = authService.login(request);
        ResponseEntity<Object> response3 = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already logged in");
        assertEquals(response3, response2);
    }

    @Test
    public void registerShopperTest() {
        RegisterShopperRequest request = new RegisterShopperRequest();
        request.setEmail("employee@mail.com");
        request.setUsername("employee");
        request.setPassword("testAuth1");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        ResponseEntity<Object> response = authService.registerShopper(request);
        ResponseEntity<Object> response2 = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or username already exists");
        assertEquals(response2, response);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        ResponseEntity<Object> response3 = authService.registerShopper(request);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(request.getEmail());
        authResponse.setUsername(request.getUsername());
        ResponseEntity<Object> response4 = ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        assertEquals(response4, response3);
    }

    @Test
    public void registerShopperTest_InvalidInput() {
        RegisterShopperRequest request = new RegisterShopperRequest();
        request.setEmail("invalidEmail");
        request.setUsername("invalidUsername");
        request.setPassword("invalidPassword");
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input, please check your email, username and password");
        ResponseEntity<Object> actualResponse = authService.registerShopper(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void logoutTest() {
        LogoutRequest request = new LogoutRequest();
        request.setEmail("employee@mail.com");
        request.setPassword("testAuth1");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setLogged(true);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        ResponseEntity<Object> response = authService.logout(request);
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("OK");
        assertEquals(expectedResponse, response);

        user.setLogged(false);
        ResponseEntity<Object> response2 = authService.logout(request);
        ResponseEntity<Object> expectedResponse2 = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already logged out");
        assertEquals(expectedResponse2, response2);
    }

    @Test
    public void registerUserTest_Employee() {
        RegisterEmployeeRequest request = new RegisterEmployeeRequest();
        request.setEmail("employee@mail.com");
        request.setUsername("employee");
        request.setPassword("testAuth1");
        request.setRole("employee");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = authService.registerUser(request);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(request.getEmail());
        authResponse.setUsername(request.getUsername());
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void registerUserTest_Shopper() {
        RegisterEmployeeRequest request = new RegisterEmployeeRequest();
        request.setEmail("shopper@mail.com");
        request.setUsername("shopper");
        request.setPassword("testAuth1");
        request.setRole("shopper");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = authService.registerUser(request);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(request.getEmail());
        authResponse.setUsername(request.getUsername());
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void registerUserTest_ExistingUser() {
        RegisterEmployeeRequest request = new RegisterEmployeeRequest();
        request.setEmail("existing@mail.com");
        request.setUsername("existing");
        request.setPassword("testAuth1");
        request.setRole("employee");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        ResponseEntity<Object> response = authService.registerUser(request);
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or username already exists");
        assertEquals(expectedResponse, response);
    }

    @Test
    public void registerUserTest_InvalidRole() {
        RegisterEmployeeRequest request = new RegisterEmployeeRequest();
        request.setEmail("invalid@mail.com");
        request.setUsername("invalid");
        request.setPassword("testAuth1");
        request.setRole("invalid");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        ResponseEntity<Object> response = authService.registerUser(request);
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
        assertEquals(expectedResponse, response);
    }

}