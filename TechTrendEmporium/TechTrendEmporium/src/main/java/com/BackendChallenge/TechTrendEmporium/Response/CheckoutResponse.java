package com.BackendChallenge.TechTrendEmporium.Response;

import com.BackendChallenge.TechTrendEmporium.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {
    private List<ProductQuantity> products;
    private Long user_id;
    private Coupon coupon;
    private double total_before_discount;
    private double total_after_discount;
    private double shipping_cost;
    private double final_total;
    private String message;
}
