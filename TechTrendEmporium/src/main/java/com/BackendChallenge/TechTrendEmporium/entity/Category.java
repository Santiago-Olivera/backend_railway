package com.BackendChallenge.TechTrendEmporium.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "category") // Table name: category
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id to identify each category

    private String name; // category name

    @Enumerated(EnumType.STRING)
    private CategoryStatus status;
    // Constructors, getters, and setters
}



