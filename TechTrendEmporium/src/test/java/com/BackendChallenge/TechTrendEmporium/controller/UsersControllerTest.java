package com.BackendChallenge.TechTrendEmporium.controller;
import com.BackendChallenge.TechTrendEmporium.Requests.UserRequest;
import com.BackendChallenge.TechTrendEmporium.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UsersController usersController;

    @Test
    public void getAllUsersTest_Success() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = usersController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteUserTest_Success() {
        UserRequest request = new UserRequest();
        request.setUsername("testUser");
        when(userService.deleteUser(any())).thenReturn(true);
        ResponseEntity<?> response = usersController.deleteUser(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteUserTest_Failure() {
        UserRequest request = new UserRequest();
        request.setUsername("testUser");
        when(userService.deleteUser(any())).thenReturn(false);
        ResponseEntity<?> response = usersController.deleteUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateUserTest_Success() {
        UserRequest request = new UserRequest();
        request.setUsername("testUser");
        request.setEmail("testEmail");
        request.setPassword("testPassword");
        when(userService.updateUser(any(), any(), any())).thenReturn(true);
        ResponseEntity<?> response = usersController.updateUser(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateUserTest_Failure() {
        UserRequest request = new UserRequest();
        request.setUsername("testUser");
        request.setEmail("testEmail");
        request.setPassword("testPassword");
        when(userService.updateUser(any(), any(), any())).thenReturn(false);
        ResponseEntity<?> response = usersController.updateUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}