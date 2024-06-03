package com.BackendChallenge.TechTrendEmporium.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterShopperRequest {
    String email;
    String username;
    String password;
}
