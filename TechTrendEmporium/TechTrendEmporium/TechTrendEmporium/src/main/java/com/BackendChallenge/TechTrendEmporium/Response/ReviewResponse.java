package com.BackendChallenge.TechTrendEmporium.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String user;
    private Long productId;
    private String comment;
    private Float rating;
}
