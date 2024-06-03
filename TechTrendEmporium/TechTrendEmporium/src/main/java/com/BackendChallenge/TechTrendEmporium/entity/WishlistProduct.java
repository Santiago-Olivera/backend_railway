package com.BackendChallenge.TechTrendEmporium.entity;



import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "wishlist_product") // Table name: wishlist_product
public class WishlistProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id to uniquely identify each wishlist-product relationship

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist; // Connects the wishlist_product table with the wishlist table

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // Connects the wishlist_product table with the product table

}
