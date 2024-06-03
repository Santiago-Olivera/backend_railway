package com.BackendChallenge.TechTrendEmporium.controller;

import com.BackendChallenge.TechTrendEmporium.Auth.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void loginTest_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("testUser");
        request.setPassword("testPassword");
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new AuthResponse());
        when(authService.login(any())).thenReturn(expectedResponse);
        Object response = authController.login(request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void registerShopperTest_Success() {
        RegisterShopperRequest request = new RegisterShopperRequest();
        request.setEmail("shopper@mail.com");
        request.setUsername("shopper");
        request.setPassword("testAuth1");
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse());
        when(authService.registerShopper(any())).thenReturn(expectedResponse);
        Object response = authController.registerShopper(request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void registerEmployeeTest_Success() {
        RegisterEmployeeRequest request = new RegisterEmployeeRequest();
        request.setEmail("employee@mail.com");
        request.setUsername("employee");
        request.setPassword("testAuth1");
        request.setRole("employee");
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse());
        when(authService.registerUser(any())).thenReturn(expectedResponse);
        Object response = authController.registerEmployee(request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void logoutTest_Success() {
        LogoutRequest request = new LogoutRequest();
        request.setEmail("testUser");
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("OK");
        when(authService.logout(any())).thenReturn(expectedResponse);
        Object response = authController.logout(request);
        assertEquals(expectedResponse, response);
    }

}
