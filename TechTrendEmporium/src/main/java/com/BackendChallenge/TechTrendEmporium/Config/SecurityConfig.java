package com.BackendChallenge.TechTrendEmporium.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.BackendChallenge.TechTrendEmporium.JWT.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import java.security.Permission;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "v3/api-docs.yaml",
            "/doc/swagger-ui/**",
            "/doc/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.cors(Customizer.withDefaults());
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers(HttpMethod.POST,"/api/auth").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/logout").permitAll()
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/admin/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/admin/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/categories").hasAnyAuthority("ADMIN", "EMPLOYEE")
                                .requestMatchers(HttpMethod.DELETE,"/api/categories").hasAnyAuthority("ADMIN","EMPLOYEE")
                                .requestMatchers(HttpMethod.PUT,"/api/categories").hasAnyAuthority("ADMIN","EMPLOYEE")
                                .requestMatchers(HttpMethod.POST,"/api/admin/category/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/products").hasAnyAuthority("ADMIN", "EMPLOYEE")
                                .requestMatchers(HttpMethod.PUT, "/api/products").hasAnyAuthority("ADMIN", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"/api/jobs").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/reviewJob").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/products").hasAnyAuthority("ADMIN", "EMPLOYEE")
                                .requestMatchers(HttpMethod.DELETE,"/api/admin/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/employee/**").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"/api/employee/**").hasAuthority("EMPLOYEE")
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}