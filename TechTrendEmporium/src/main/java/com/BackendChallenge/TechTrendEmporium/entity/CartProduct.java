package com.BackendChallenge.TechTrendEmporium.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "cart_product") // Table name: cart_product
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id to uniquely identify each cart-product relationship

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart; // cart_id: connect with the cart table

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // product_id: connect with the product table

    private Integer quantity;

}

