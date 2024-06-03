package com.BackendChallenge.TechTrendEmporium.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "product") // Table name: product
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id to uniquely identify the product

    private String title; // title should be a string

    private double price; // price should be a double

    @Column(name = "category")
    private String category; // Treat category as a string
    // category_id: connect with the category table
    @Column(name = "category_id")
    private Long categoryId ;

    private String description; // description should be a string

    private String image; // image should be a string

    @Embedded
    private Rating rating; // Rating information

    @Embedded
    private Inventory inventory;


    @Data
    @Embeddable
    public static class Rating {
        private double rate;
        private int count;
    }

    @Data
    @Embeddable
    public static class Inventory {
        private int total;
        private int available;
    }


    @Enumerated(EnumType.STRING)
    private ProductStatus status;
}
