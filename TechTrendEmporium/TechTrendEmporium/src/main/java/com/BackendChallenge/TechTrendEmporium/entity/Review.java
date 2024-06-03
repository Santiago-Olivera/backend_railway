package com.BackendChallenge.TechTrendEmporium.entity;


import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table(name = "review") // Table name: review
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id to uniquely identify each review

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // One user can have multiple reviews, one review can only have one user

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // Multiple reviews can be related to one specific product

    private String comment; // comment should be a string

    private float rating; // rating should be a float
}

