package com.BackendChallenge.TechTrendEmporium.service;
import com.BackendChallenge.TechTrendEmporium.entity.Coupon;
import com.BackendChallenge.TechTrendEmporium.repository.CouponRepository;
import com.BackendChallenge.TechTrendEmporium.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addCouponTest_CouponDoesNotExist() {
        when(couponRepository.findByName(anyString())).thenReturn(Optional.empty());
        assertTrue(couponService.addCoupon("coupon1", 10));
        verify(couponRepository, times(1)).save(any());
    }

    @Test
    public void addCouponTest_CouponExists() {
        when(couponRepository.findByName(anyString())).thenReturn(Optional.of(new Coupon()));
        assertFalse(couponService.addCoupon("coupon1", 10));
        verify(couponRepository, times(0)).save(any());
    }

    @Test
    public void deleteCouponTest_CouponExists() {
        when(couponRepository.findByName(anyString())).thenReturn(Optional.of(new Coupon()));
        assertTrue(couponService.deleteCoupon("coupon1"));
        verify(couponRepository, times(1)).delete(any());
    }

    @Test
    public void deleteCouponTest_CouponDoesNotExist() {
        when(couponRepository.findByName(anyString())).thenReturn(Optional.empty());
        assertFalse(couponService.deleteCoupon("coupon1"));
        verify(couponRepository, times(0)).delete(any());
    }

    @Test
    public void existsCouponTest() {
        when(couponRepository.findByName(anyString())).thenReturn(Optional.of(new Coupon()));
        assertTrue(couponService.existsCoupon("coupon1"));
    }

    @Test
    public void getCouponsTest() {
        when(couponRepository.findAll()).thenReturn(Arrays.asList(new Coupon(), new Coupon()));
        assertEquals(2, couponService.getCoupons().size());
    }
}