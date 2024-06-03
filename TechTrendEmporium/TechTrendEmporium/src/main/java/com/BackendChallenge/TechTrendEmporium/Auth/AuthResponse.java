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
public class AuthResponse {
    String token;
    String email;
    String username;
}
