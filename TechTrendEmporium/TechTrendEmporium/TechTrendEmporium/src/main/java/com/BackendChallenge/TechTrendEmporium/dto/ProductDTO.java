package com.BackendChallenge.TechTrendEmporium.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String title;
    private double price;
    private String category;
    private String description;
    private String image;
    private RatingDTO rating;
    private InventoryDTO inventory;

    @Data
    public static class RatingDTO {
        private double rate;
        private int count;

        public RatingDTO(double rate, int count) {
            this.rate = rate;
            this.count = count;
        }
    }

    @Data
    public static class InventoryDTO {
        private int total;
        private int available;

        public InventoryDTO(int total, int available) {
            this.total = total;
            this.available = available;
        }
    }




}

