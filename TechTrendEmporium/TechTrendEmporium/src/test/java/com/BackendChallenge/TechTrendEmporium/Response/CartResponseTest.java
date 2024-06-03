package com.BackendChallenge.TechTrendEmporium.Response;
import com.BackendChallenge.TechTrendEmporium.entity.Coupon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

public class CartResponseTest {

    @Test
    public void testCartResponse() {
        // Create a new instance of ProductQuantity
        ProductQuantity productQuantity = ProductQuantity.builder()
                .productId(1L)
                .quantity(10)
                .build();

        // Create a list of ProductQuantity
        List<ProductQuantity> products = Arrays.asList(productQuantity);

        // Create a new instance of Coupon
        Coupon coupon = new Coupon();
        coupon.setName("TEST");

        // Create a new instance of CartResponse using the builder
        CartResponse cartResponse = CartResponse.builder()
                .products(products)
                .user_id(1L)
                .coupon(coupon)
                .build();

        // Assert that the products, user_id, and coupon are set correctly
        assertNotNull(cartResponse.getProducts());
        assertEquals(1, cartResponse.getProducts().size());
        assertEquals(1L, cartResponse.getUser_id());
        assertNotNull(cartResponse.getCoupon());
        assertEquals("TEST", cartResponse.getCoupon().getName());

        // Change the values
        cartResponse.setUser_id(2L);
        coupon.setName("NEW_TEST");
        cartResponse.setCoupon(coupon);

        // Assert that the user_id and coupon have been updated
        assertEquals(2L, cartResponse.getUser_id());
        assertEquals("NEW_TEST", cartResponse.getCoupon().getName());
    }
}