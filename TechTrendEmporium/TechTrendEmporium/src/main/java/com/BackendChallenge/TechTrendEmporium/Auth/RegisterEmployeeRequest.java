package com.BackendChallenge.TechTrendEmporium.Auth;

import com.BackendChallenge.TechTrendEmporium.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEmployeeRequest {
    String email;
    String username;
    String password;
    String role;
}