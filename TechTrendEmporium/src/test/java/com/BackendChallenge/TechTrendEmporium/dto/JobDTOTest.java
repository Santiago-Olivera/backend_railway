package com.BackendChallenge.TechTrendEmporium.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JobDTOTest {

    @Test
    void testAccessors() {
        // Create a JobDTO instance
        JobDTO jobDTO = new JobDTO("Type", 1L, "Operation");

        // Test accessor methods
        assertNotNull(jobDTO.getType());
        assertEquals("Type", jobDTO.getType());

        assertNotNull(jobDTO.getId());
        assertEquals(1L, jobDTO.getId());

        assertNotNull(jobDTO.getOperation());
        assertEquals("Operation", jobDTO.getOperation());
    }

    @Test
    void testSetterMethods() {
        // Create a JobDTO instance
        JobDTO jobDTO = new JobDTO();

        // Set values using setter methods
        jobDTO.setType("Type");
        jobDTO.setId(1L);
        jobDTO.setOperation("Operation");

        // Test accessor methods after setting values
        assertEquals("Type", jobDTO.getType());
        assertEquals(1L, jobDTO.getId());
        assertEquals("Operation", jobDTO.getOperation());
    }

}

