package com.BackendChallenge.TechTrendEmporium.controller;

import com.BackendChallenge.TechTrendEmporium.Requests.ApplyCouponRequest;
import com.BackendChallenge.TechTrendEmporium.Requests.CartRequest;
import com.BackendChallenge.TechTrendEmporium.service.CartService;
import com.BackendChallenge.TechTrendEmporium.Response.CartResponse;
import com.BackendChallenge.TechTrendEmporium.Response.CheckoutResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Test
    public void getCartsTest_Success() {
        CartRequest request = new CartRequest();
        request.setUser_id(1L);
        when(cartService.getCartByUser(any())).thenReturn(new CartResponse());
        ResponseEntity<?> response = cartController.getCarts(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addProductToCartTest_Success() {
        CartRequest request = new CartRequest();
        request.setUser_id(1L);
        request.setProduct_id(1L);
        request.setQuantity(1);
        when(cartService.addProductToCart(anyLong(), anyLong(), anyInt())).thenReturn(true);
        ResponseEntity<?> response = cartController.addProductToCart(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void removeProductFromCartTest_Success() {
        CartRequest request = new CartRequest();
        request.setUser_id(1L);
        request.setProduct_id(1L);
        request.setQuantity(1);
        when(cartService.deleteProductFromCart(anyLong(), anyLong(), anyInt())).thenReturn(true);
        ResponseEntity<?> response = cartController.removeProductFromCart(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void checkoutTest_Success() {
        CartRequest request = new CartRequest();
        request.setUser_id(1L);
        CheckoutResponse checkoutResponse = new CheckoutResponse();
        checkoutResponse.setMessage("Checkout successful");
        when(cartService.checkout(any())).thenReturn(checkoutResponse);
        ResponseEntity<?> response = cartController.checkout(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void applyCouponTest_Success() {
        ApplyCouponRequest request = new ApplyCouponRequest();
        request.setUser_id(1L);
        request.setCoupon_code("COUPON");
        when(cartService.applyCoupon(any(), any())).thenReturn(true);
        ResponseEntity<?> response = cartController.applyCoupon(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getCartsTest_Failure() {
        CartRequest request = new CartRequest();
        request.setUser_id(1L);
        when(cartService.getCartByUser(any())).thenReturn(null);
        ResponseEntity<?> response = cartController.getCarts(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void addProductToCartTest_Failure() {
        CartRequest request = new CartRequest();
        request.setUser_id(1L);
        request.setProduct_id(1L);
        request.setQuantity(1);
        when(cartService.addProductToCart(anyLong(), anyLong(), anyInt())).thenReturn(false);
        ResponseEntity<?> response = cartController.addProductToCart(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void removeProductFromCartTest_Failure() {
        CartRequest request = new CartRequest();
        request.setUser_id(1L);
        request.setProduct_id(1L);
        request.setQuantity(1);
        when(cartService.deleteProductFromCart(anyLong(), anyLong(), anyInt())).thenReturn(false);
        ResponseEntity<?> response = cartController.removeProductFromCart(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void checkoutTest_Failure() {
        CartRequest request = new CartRequest();
        request.setUser_id(1L);
        CheckoutResponse checkoutResponse = new CheckoutResponse();
        checkoutResponse.setMessage("Checkout failed");
        when(cartService.checkout(any())).thenReturn(checkoutResponse);
        ResponseEntity<?> response = cartController.checkout(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void applyCouponTest_Failure() {
        ApplyCouponRequest request = new ApplyCouponRequest();
        request.setUser_id(1L);
        request.setCoupon_code("COUPON");
        when(cartService.applyCoupon(any(), any())).thenReturn(false);
        ResponseEntity<?> response = cartController.applyCoupon(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}