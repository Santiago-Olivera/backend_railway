package com.BackendChallenge.TechTrendEmporium.Response;

import com.BackendChallenge.TechTrendEmporium.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {
    private List<Long> products;
    private Long user_id;
}
