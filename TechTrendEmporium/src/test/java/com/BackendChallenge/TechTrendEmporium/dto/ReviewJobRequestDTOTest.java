package com.BackendChallenge.TechTrendEmporium.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReviewJobRequestDTOTest {

    @Test
    void testAccessors() {
        // Create a ReviewJobRequestDTO instance
        ReviewJobRequestDTO requestDTO = new ReviewJobRequestDTO();
        requestDTO.setId(1L);
        requestDTO.setOperation("create");
        requestDTO.setAction("approve");

        // Test accessor methods
        assertNotNull(requestDTO.getId());
        assertEquals(1L, requestDTO.getId());
        assertNotNull(requestDTO.getOperation());
        assertEquals("create", requestDTO.getOperation());
        assertNotNull(requestDTO.getAction());
        assertEquals("approve", requestDTO.getAction());
    }
}

