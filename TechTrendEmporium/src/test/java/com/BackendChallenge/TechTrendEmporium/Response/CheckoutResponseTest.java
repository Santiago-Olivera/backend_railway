package com.BackendChallenge.TechTrendEmporium.Response;
import com.BackendChallenge.TechTrendEmporium.entity.Coupon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

public class CheckoutResponseTest {

    @Test
    public void testCheckoutResponse() {
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

        // Create a new instance of CheckoutResponse using the builder
        CheckoutResponse checkoutResponse = CheckoutResponse.builder()
                .products(products)
                .user_id(1L)
                .coupon(coupon)
                .total_before_discount(100.0)
                .total_after_discount(90.0)
                .shipping_cost(10.0)
                .final_total(100.0)
                .message("Checkout successful")
                .build();

        // Assert that the products, user_id, coupon, totals, and message are set correctly
        assertNotNull(checkoutResponse.getProducts());
        assertEquals(1, checkoutResponse.getProducts().size());
        assertEquals(1L, checkoutResponse.getUser_id());
        assertNotNull(checkoutResponse.getCoupon());
        assertEquals("TEST", checkoutResponse.getCoupon().getName());
        assertEquals(100.0, checkoutResponse.getTotal_before_discount());
        assertEquals(90.0, checkoutResponse.getTotal_after_discount());
        assertEquals(10.0, checkoutResponse.getShipping_cost());
        assertEquals(100.0, checkoutResponse.getFinal_total());
        assertEquals("Checkout successful", checkoutResponse.getMessage());

        // Change the values
        checkoutResponse.setUser_id(2L);
        coupon.setName("NEW_TEST");
        checkoutResponse.setCoupon(coupon);
        checkoutResponse.setTotal_before_discount(200.0);
        checkoutResponse.setTotal_after_discount(180.0);
        checkoutResponse.setShipping_cost(20.0);
        checkoutResponse.setFinal_total(200.0);
        checkoutResponse.setMessage("Checkout updated");

        // Assert that the user_id, coupon, totals, and message have been updated
        assertEquals(2L, checkoutResponse.getUser_id());
        assertEquals("NEW_TEST", checkoutResponse.getCoupon().getName());
        assertEquals(200.0, checkoutResponse.getTotal_before_discount());
        assertEquals(180.0, checkoutResponse.getTotal_after_discount());
        assertEquals(20.0, checkoutResponse.getShipping_cost());
        assertEquals(200.0, checkoutResponse.getFinal_total());
        assertEquals("Checkout updated", checkoutResponse.getMessage());
    }
}