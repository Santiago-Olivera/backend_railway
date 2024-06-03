package com.BackendChallenge.TechTrendEmporium.Auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.management.ObjectName;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "login")
    public Object login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }


    @PostMapping(value = "auth")
    public Object registerShopper(@RequestBody RegisterShopperRequest request)
    {
        return authService.registerShopper(request);
    }

    @PostMapping(value = "admin/auth")
    public Object registerEmployee(@RequestBody RegisterEmployeeRequest request)
    {
        return authService.registerUser(request);
    }

    @PostMapping(value = "logout")
    public Object logout(@RequestBody LogoutRequest request)
    {
        return authService.logout(request);
    }

}
