package com.BackendChallenge.TechTrendEmporium.controller;

import com.BackendChallenge.TechTrendEmporium.Requests.ApplyCouponRequest;
import com.BackendChallenge.TechTrendEmporium.Requests.CartRequest;
import com.BackendChallenge.TechTrendEmporium.service.CartService;
import com.BackendChallenge.TechTrendEmporium.Response.CartResponse;
import com.BackendChallenge.TechTrendEmporium.Response.CheckoutResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping(value = "all")
    public ResponseEntity<?> getCarts(@RequestBody CartRequest request) {
        CartResponse response = cartService.getCartByUser(request.getUser_id());
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping(value = "add")
    public ResponseEntity<?> addProductToCart(@RequestBody CartRequest request){
        boolean added = cartService.addProductToCart(request.getUser_id(), request.getProduct_id(), request.getQuantity());
        if (added) {
            return ResponseEntity.created(null).body("Product added to cart!");
        } else {
            return ResponseEntity.badRequest().body("ERROR : Product not added to cart.");
        }
    }

    @DeleteMapping(value = "remove")
    public ResponseEntity<?> removeProductFromCart(@RequestBody CartRequest request){
        boolean removed = cartService.deleteProductFromCart(request.getUser_id(), request.getProduct_id(), request.getQuantity());
        if (removed) {
            return ResponseEntity.ok("Product removed from cart");
        } else {
            return ResponseEntity.badRequest().body("User or product not found. Product not removed from cart.");
        }
    }

    @PostMapping(value = "checkout")
    public ResponseEntity<?> checkout(@RequestBody CartRequest request){
        CheckoutResponse checkedOut = cartService.checkout(request.getUser_id());
        if (checkedOut.getMessage().equals("Checkout successful")) {
            return ResponseEntity.ok(checkedOut);
        } else {
            return ResponseEntity.badRequest().body(checkedOut.getMessage());
        }
    }

    @PostMapping(value = "apply-coupon")
    public ResponseEntity<?> applyCoupon(@RequestBody ApplyCouponRequest request){
        boolean applied = cartService.applyCoupon(request.getUser_id(), request.getCoupon_code());
        if (applied) {
            return ResponseEntity.ok("Coupon applied successfully");
        } else {
            return ResponseEntity.badRequest().body("Coupon does not exist");
        }
    }
}
