package com.BackendChallenge.TechTrendEmporium.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewRequest {
    private String user;
    private String comment;
    private Float rating;
}