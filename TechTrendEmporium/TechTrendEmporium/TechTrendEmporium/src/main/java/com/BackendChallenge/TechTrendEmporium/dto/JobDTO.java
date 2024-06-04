package com.BackendChallenge.TechTrendEmporium.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private String type;
    private Long id;
    private String operation;
}


