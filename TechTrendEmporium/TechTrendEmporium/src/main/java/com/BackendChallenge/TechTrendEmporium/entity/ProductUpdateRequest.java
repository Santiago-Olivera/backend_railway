package com.BackendChallenge.TechTrendEmporium.entity;

import lombok.Data;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String title;
    private double price;
    private String category;
    private String description;
    private String image;
    private Rating rating;
    private Inventory inventory;

    @Data
    public static class Rating {
        private double rate;
        private int count;
    }

    @Data
    public static class Inventory {
        private int total;
        private int available;
    }
}
