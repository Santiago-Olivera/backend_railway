package com.BackendChallenge.TechTrendEmporium.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryDTOTest {

    @Test
    void testAccessors() {
        // Create a CategoryDTO instance
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Test Category");

        // Test accessor methods
        assertNotNull(categoryDTO.getId());
        assertNotNull(categoryDTO.getName());
    }
}
