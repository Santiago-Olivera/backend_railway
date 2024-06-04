package com.BackendChallenge.TechTrendEmporium.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewJobRequestDTO {
    private Long id;
    private String operation;
    private String action;
}
