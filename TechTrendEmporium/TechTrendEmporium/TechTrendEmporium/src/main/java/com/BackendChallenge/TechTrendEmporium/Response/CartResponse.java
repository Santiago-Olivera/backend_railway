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
public class CartResponse {
    private List<ProductQuantity> products;
    private Long user_id;
    private Coupon coupon;
}