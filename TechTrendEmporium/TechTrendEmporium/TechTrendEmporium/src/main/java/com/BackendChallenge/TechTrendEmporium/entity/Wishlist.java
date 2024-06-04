package com.BackendChallenge.TechTrendEmporium.entity;

import com.BackendChallenge.TechTrendEmporium.entity.User;
import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table (name = "wishlist") // Table name: wishlist
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id to uniquely identify each wishlist

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true) // Connects this table with the user table
    private User user; // One user can only have one wishlist, and one wishlist can only belong to one user
}
