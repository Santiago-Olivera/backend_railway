package com.BackendChallenge.TechTrendEmporium.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "sale") // Table name: sale
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id to uniquely identify each sale

    private String date; // date: a string

    private double total; // total: a float

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", unique = true)
    private Cart cart; // cart_id: connect with the cart table (one-to-one relationship)

    private SaleStatus Status;
}

