package com.BackendChallenge.TechTrendEmporium.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BackendChallenge.TechTrendEmporium.repository.CouponRepository;
import com.BackendChallenge.TechTrendEmporium.entity.Coupon;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public boolean addCoupon( String name, Integer discount) {
        if (existsCoupon(name)) {
            return false;
        }
        Coupon newCoupon = new Coupon();
        newCoupon.setName(name);
        newCoupon.setDiscountPercentage(discount);
        couponRepository.save(newCoupon);
        return true;
    }

    public boolean deleteCoupon( String name) {
        if (existsCoupon(name)) {
            Optional<Coupon> coupon = couponRepository.findByName(name);
            couponRepository.delete(coupon.get());
            return true;
        }
        return false;
    }

    public boolean existsCoupon(String name) {
        Optional<Coupon> coupon = couponRepository.findByName(name);
        return coupon.isPresent();
    }


    public List<Coupon> getCoupons() {
        return couponRepository.findAll();
    }

}
