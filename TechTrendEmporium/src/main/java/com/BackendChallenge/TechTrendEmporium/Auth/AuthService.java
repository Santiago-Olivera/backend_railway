package com.BackendChallenge.TechTrendEmporium.Auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BackendChallenge.TechTrendEmporium.entity.User;
import com.BackendChallenge.TechTrendEmporium.repository.UserRepository;
import com.BackendChallenge.TechTrendEmporium.JWT.JwtService;
import com.BackendChallenge.TechTrendEmporium.entity.Role;

import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Object> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        String name = user.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        user.setUsername(name);
        if (user.isLogged()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already logged in");
        } else {
            user.setLogged(true);
            userRepository.save(user);
        }
        String token = jwtService.getToken(user);
        user.setLogged(true);
        userRepository.save(user);
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .username(name)
                .build());
    }



    public ResponseEntity<Object> registerShopper(RegisterShopperRequest request) {

        String regexEmail = "^(.+)@(.+)$";
        String regexUsername = "^[a-zA-Z0-9_]{5,20}$";
        String regexPassword = "^[a-zA-Z0-9_]{8,20}$";
        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();

        if (!Pattern.matches(regexEmail, email) || !Pattern.matches(regexUsername, username) || !Pattern.matches(regexPassword, password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input, please check your email, username and password");
        }
        if (userRepository.findByEmail(email).isPresent() || userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or username already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode( request.getPassword()))
                .role(Role.SHOPPER)
                .build();

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
                .token(jwtService.getToken(user))
                .email(user.getEmail())
                .username(user.getUsername())
                .build());
    }

    public ResponseEntity<Object> registerUser(RegisterEmployeeRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent() || userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or username already exists");
        }
        if (Objects.equals(request.getRole().toLowerCase(), "employee")) {
            User user = User.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode( request.getPassword()))
                    .role(Role.EMPLOYEE)
                    .build();
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
                    .token(jwtService.getToken(user))
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build());
        }
        else if (Objects.equals(request.getRole().toLowerCase(), "shopper")) {
            User user = User.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode( request.getPassword()))
                    .role(Role.SHOPPER)
                    .build();
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
                    .token(jwtService.getToken(user))
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build());
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
        }
    }

    public ResponseEntity<Object> logout(LogoutRequest request) {
        User user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("User not found"));
        String name = user.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        user.setUsername(name);
        if (user.isLogged()) {
            user.setLogged(false);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already logged out");
        }
    }
}