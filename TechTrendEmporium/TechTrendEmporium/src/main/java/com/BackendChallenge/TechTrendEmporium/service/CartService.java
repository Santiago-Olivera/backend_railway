package com.BackendChallenge.TechTrendEmporium.service;

import com.BackendChallenge.TechTrendEmporium.entity.*;
import com.BackendChallenge.TechTrendEmporium.repository.*;
import com.BackendChallenge.TechTrendEmporium.Response.CartResponse;
import com.BackendChallenge.TechTrendEmporium.Response.CheckoutResponse;
import com.BackendChallenge.TechTrendEmporium.Response.ProductQuantity;
import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private SaleRepository saleRepository;

    public boolean addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = existsCart(userId, "OPEN");
        System.out.println("Cart: " + cart);
        if (cart == null) {
            return false;
        }
        Optional<Product> product = productRepository.findById(productId);
        System.out.println("Product: " + product);
        Optional<CartProduct> cartProductV = Optional.ofNullable(cartProductRepository.findByCartIdAndProductId(cart.getId(), productId));
        System.out.println("CartProduct: " + cartProductV);
        CartProduct cartProduct = new CartProduct();
        if (product.isPresent()) {
            cartProduct.setCart(cart);
            cartProduct.setProduct(product.get());
            if (cartProductV.isPresent()) {
                cartProduct.setQuantity(cartProductV.get().getQuantity() + quantity);
                if (cartProduct.getQuantity() > product.get().getInventory().getAvailable()) {
                    return false;
                }
                cartProductRepository.updateQuantity(cartProduct.getQuantity(), cartProductV.get().getId());
            } else {
                if (quantity > product.get().getInventory().getAvailable()) {
                    return false;
                }
                cartProduct.setQuantity(quantity);
                cartProductRepository.save(cartProduct);
            }
            return true;
        }
        return false;
    }

    public boolean deleteProductFromCart(Long userId, Long productId, int quantity) {
        Cart cart = existsCart(userId, "OPEN");
        if (cart == null) {
            return false;
        }
        Optional<CartProduct> cartProduct = Optional.ofNullable(cartProductRepository.findByCartIdAndProductId(cart.getId(), productId));
        if (cartProduct.isPresent()) {
            if (cartProduct.get().getQuantity() <= quantity) {
                cartProductRepository.delete(cartProduct.get());
            } else {
                cartProduct.get().setQuantity(cartProduct.get().getQuantity() - quantity);
                cartProductRepository.updateQuantity(cartProduct.get().getQuantity(), cartProduct.get().getId());
            }
            return true;
        }
        return false;
    }

    public CartResponse getCartByUser(Long userId) {
        Cart cart = existsCart(userId, "OPEN");
        if (cart == null) {
            return null;
        }
        CartResponse response = new CartResponse();
        List<CartProduct> cartProducts = cartProductRepository.findByCartId(cart.getId());
        List<ProductQuantity> products = new ArrayList<>();
        for (CartProduct cartProduct : cartProducts) {
            ProductQuantity productQuantity = new ProductQuantity(cartProduct.getProduct().getId(), cartProduct.getQuantity());
            products.add(productQuantity);
        }
        response.setUser_id(userId);
        response.setProducts(products);
        Optional<Coupon> coupon = Optional.ofNullable(cart.getCoupon());
        coupon.ifPresent(response::setCoupon);
        return response;
    }

    public Cart existsCart(Long userId, String status) {
        Optional<Cart> cart = cartRepository.findByUserIdAndStatus(userId, status);
        if (cart.isPresent() && Objects.equals(cart.get().getStatus(), "OPEN")) {
            return cart.get();
        }
        else {
            Cart newCart = new Cart();
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                return null;
            }
            newCart.setUser(user.get());
            newCart.setStatus("OPEN");
            cartRepository.save(newCart);
            return newCart;
        }
    }

    public CheckoutResponse checkout(Long userId) {
        Cart cart = existsCart(userId, "OPEN");
        CheckoutResponse response = new CheckoutResponse();
        if (cart == null) {
            response.setMessage("Checkout failed, cart not found.");
            return response;
        }
        List<CartProduct> cartProducts = cartProductRepository.findByCartId(cart.getId());
        double totalBeforeDiscount = 0.0;
        Optional<Integer> discount = cart.getCoupon().getDiscountPercentage().describeConstable();
        double totalAfterDiscount = 0.0;
        double shipping = 19.99;
        double finalTotal = 0.0;
        List<ProductQuantity> products = new ArrayList<>();
        for (CartProduct cartProduct : cartProducts) {
            totalBeforeDiscount += cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
            products.add(new ProductQuantity(cartProduct.getProduct().getId(), cartProduct.getQuantity()));
        }
        if (discount.isPresent()) {
            totalAfterDiscount = totalBeforeDiscount - (totalBeforeDiscount * discount.get() / 100);
        }
        finalTotal = totalAfterDiscount + shipping;
        for (CartProduct cartProduct : cartProducts) {
            if (cartProduct.getQuantity() > cartProduct.getProduct().getInventory().getAvailable()) {
                response.setMessage("Checkout failed, not enough stock.");
                return response;
            }
            cartProduct.getProduct().getInventory().setAvailable(cartProduct.getProduct().getInventory().getAvailable() - cartProduct.getQuantity());
            productRepository.save(cartProduct.getProduct());
        }
        cart.setStatus("CLOSED");
        cartRepository.save(cart);
        Sale sale = new Sale();
        sale.setCart(cart);
        sale.setTotal(finalTotal);
        sale.setStatus(SaleStatus.APPROVED);
        sale.setDate(String.valueOf(LocalDate.now()));
        saleRepository.save(sale);
        response.setProducts(products);
        response.setUser_id(userId);
        Optional<Coupon> coupon = Optional.ofNullable(cart.getCoupon());
        coupon.ifPresent(response::setCoupon);
        response.setTotal_before_discount(totalBeforeDiscount);
        response.setTotal_after_discount(totalAfterDiscount);
        response.setShipping_cost(shipping);
        response.setFinal_total(finalTotal);
        response.setMessage("Checkout successful");
        return response;
    }

    public boolean applyCoupon(Long userId, String couponName) {
        Cart cart = existsCart(userId, "OPEN");
        if (cart == null) {
            return false;
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            if (couponRepository.findByName(couponName).isPresent()) {
                cart.setCoupon(couponRepository.findByName(couponName).get());
                cartRepository.save(cart);
                return true;
            }
        }
        return false;
    }
}
