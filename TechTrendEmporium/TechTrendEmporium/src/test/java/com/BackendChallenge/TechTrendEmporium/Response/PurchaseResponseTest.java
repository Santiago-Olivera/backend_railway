package com.BackendChallenge.TechTrendEmporium.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchaseResponseTest {

    @Test
    public void testPurchaseResponse() {
        // Create a new instance of PurchaseResponse using the builder
        PurchaseResponse purchaseResponse = PurchaseResponse.builder()
                .saleId(1L)
                .userId(1L)
                .total(100.0)
                .date("2022-01-01")
                .status("Completed")
                .build();

        // Assert that the saleId, userId, total, date, and status are set correctly
        assertEquals(1L, purchaseResponse.getSaleId());
        assertEquals(1L, purchaseResponse.getUserId());
        assertEquals(100.0, purchaseResponse.getTotal());
        assertEquals("2022-01-01", purchaseResponse.getDate());
        assertEquals("Completed", purchaseResponse.getStatus());

        // Change the values
        purchaseResponse.setSaleId(2L);
        purchaseResponse.setUserId(2L);
        purchaseResponse.setTotal(200.0);
        purchaseResponse.setDate("2022-02-02");
        purchaseResponse.setStatus("Pending");

        // Assert that the saleId, userId, total, date, and status have been updated
        assertEquals(2L, purchaseResponse.getSaleId());
        assertEquals(2L, purchaseResponse.getUserId());
        assertEquals(200.0, purchaseResponse.getTotal());
        assertEquals("2022-02-02", purchaseResponse.getDate());
        assertEquals("Pending", purchaseResponse.getStatus());
    }
}