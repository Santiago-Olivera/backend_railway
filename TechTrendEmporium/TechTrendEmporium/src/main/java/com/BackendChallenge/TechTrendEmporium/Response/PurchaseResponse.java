package com.BackendChallenge.TechTrendEmporium.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private Long saleId;
    private Long userId;
    private double total;
    private String date;
    private String status;
}
