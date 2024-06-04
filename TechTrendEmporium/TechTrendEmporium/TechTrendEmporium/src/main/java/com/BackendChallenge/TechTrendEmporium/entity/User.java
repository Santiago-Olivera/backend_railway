package com.BackendChallenge.TechTrendEmporium.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table (name = "user") // Table name: user
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique id, to identify each user

    private String name; // name that would be a string

    @Column(unique = true)
    private String username; // username that would be a string (should be unique)

    @Column(unique = true)
    private String email; // email that should be a string (should be unique)

    private String password; // password

    @Enumerated(EnumType.STRING)
    Role role;

    private boolean logged; // logged that should be a boolean

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.name())));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

}

