package com.BackendChallenge.TechTrendEmporium.controller;

import com.BackendChallenge.TechTrendEmporium.Requests.AddCouponRequest;
import com.BackendChallenge.TechTrendEmporium.Requests.DeleteCouponRequest;
import com.BackendChallenge.TechTrendEmporium.entity.Coupon;
import com.BackendChallenge.TechTrendEmporium.service.CouponService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponControllerTest {

    @Mock
    private CouponService couponService;

    @InjectMocks
    private CouponController couponController;

    @Test
    public void addCouponTest_Success() {
        AddCouponRequest request = new AddCouponRequest();
        request.setName("testCoupon");
        request.setDiscount(10);
        when(couponService.addCoupon(any(), any())).thenReturn(true);
        ResponseEntity<?> response = couponController.addCoupon(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void deleteCouponTest_Success() {
        DeleteCouponRequest request = new DeleteCouponRequest();
        request.setName("testCoupon");
        when(couponService.deleteCoupon(any())).thenReturn(true);
        ResponseEntity<?> response = couponController.deleteCoupon(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getCouponsTest_Success() {
        Coupon coupon = new Coupon();
        coupon.setName("testCoupon");
        coupon.setDiscountPercentage(10);
        when(couponService.getCoupons()).thenReturn(Collections.singletonList(coupon));
        List<Coupon> response = couponController.getCoupons();
        assertEquals(1, response.size());
        assertEquals("testCoupon", response.get(0).getName());
        assertEquals(10, response.get(0).getDiscountPercentage());
    }

    @Test
    public void addCouponTest_Failure() {
        AddCouponRequest request = new AddCouponRequest();
        request.setName("testCoupon");
        request.setDiscount(10);
        when(couponService.addCoupon(any(), any())).thenReturn(false);
        ResponseEntity<?> response = couponController.addCoupon(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteCouponTest_Failure() {
        DeleteCouponRequest request = new DeleteCouponRequest();
        request.setName("testCoupon");
        when(couponService.deleteCoupon(any())).thenReturn(false);
        ResponseEntity<?> response = couponController.deleteCoupon(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}