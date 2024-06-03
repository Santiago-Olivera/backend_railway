package com.BackendChallenge.TechTrendEmporium.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "coupon") // Table name: coupon
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id to uniquely identify each coupon

    private String name; // name is a string

    private Integer discountPercentage; // discount percentage is an integer
}

