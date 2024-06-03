package com.BackendChallenge.TechTrendEmporium.Response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductQuantityTest {

    @Test
    public void testProductQuantity() {
        // Create a new instance of ProductQuantity using the builder
        ProductQuantity productQuantity = ProductQuantity.builder()
                .productId(1L)
                .quantity(10)
                .build();

        // Assert that the productId and quantity are set correctly
        assertEquals(1L, productQuantity.getProductId());
        assertEquals(10, productQuantity.getQuantity());

        // Change the values
        productQuantity.setProductId(2L);
        productQuantity.setQuantity(20);

        // Assert that the productId and quantity have been updated
        assertEquals(2L, productQuantity.getProductId());
        assertEquals(20, productQuantity.getQuantity());
    }
}