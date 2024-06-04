package com.BackendChallenge.TechTrendEmporium.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductDTOTest {

    @Test
    void testAccessors() {
        // Create a RatingDTO instance
        ProductDTO.RatingDTO ratingDTO = new ProductDTO.RatingDTO(4.5, 100);

        // Create an InventoryDTO instance
        ProductDTO.InventoryDTO inventoryDTO = new ProductDTO.InventoryDTO(500, 300);

        // Create a ProductDTO instance
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setTitle("Test Product");
        productDTO.setPrice(99.99);
        productDTO.setCategory("Electronics");
        productDTO.setDescription("Test description");
        productDTO.setImage("test.jpg");
        productDTO.setRating(ratingDTO);
        productDTO.setInventory(inventoryDTO);

        // Test accessor methods
        assertNotNull(productDTO.getId());
        assertNotNull(productDTO.getTitle());
        //noinspection ObviousNullCheck
        assertNotNull(productDTO.getPrice());
        assertNotNull(productDTO.getCategory());
        assertNotNull(productDTO.getDescription());
        assertNotNull(productDTO.getImage());
        assertNotNull(productDTO.getRating());
        assertNotNull(productDTO.getInventory());
        //noinspection ObviousNullCheck
        assertNotNull(productDTO.getRating().getRate());
        //noinspection ObviousNullCheck
        assertNotNull(productDTO.getRating().getCount());
        //noinspection ObviousNullCheck
        assertNotNull(productDTO.getInventory().getTotal());
        //noinspection ObviousNullCheck
        assertNotNull(productDTO.getInventory().getAvailable());
    }
}

