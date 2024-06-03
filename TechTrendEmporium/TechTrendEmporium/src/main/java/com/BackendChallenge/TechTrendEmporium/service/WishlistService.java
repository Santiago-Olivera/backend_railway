package com.BackendChallenge.TechTrendEmporium.service;


import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.Wishlist;
import com.BackendChallenge.TechTrendEmporium.repository.ProductRepository;
import com.BackendChallenge.TechTrendEmporium.repository.UserRepository;
import com.BackendChallenge.TechTrendEmporium.entity.WishlistProduct;
import com.BackendChallenge.TechTrendEmporium.repository.WishlistProductRepository;
import com.BackendChallenge.TechTrendEmporium.repository.WishlistRepository;
import com.BackendChallenge.TechTrendEmporium.Response.WishlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishlistProductRepository wishlistProductRepository;
    @Autowired
    private ProductRepository productRepository;

    public WishlistResponse getWishlistByUser(Long userId) {
        Optional<Wishlist> wishlist = wishlistRepository.findByUserId(userId);
        if (wishlist.isEmpty()) {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(userRepository.findById(userId).orElse(null));
            wishlistRepository.save(newWishlist);
            return null;
        }
        List<WishlistProduct> wishlistProducts = wishlistProductRepository.findByWishlistId(wishlist.get().getId());
        WishlistResponse response = new WishlistResponse();
        List<Long> products = new ArrayList<>();
        for (WishlistProduct wishlistProduct : wishlistProducts) {
            products.add(wishlistProduct.getProduct().getId());
        }
        response.setProducts(products);
        response.setUser_id(userId);
        return response;
    }

    public boolean addProductToWishlist(Long userId, Long productId) {
        Optional<Wishlist> wishlist = wishlistRepository.findByUserId(userId);
        WishlistProduct wishlistProduct = new WishlistProduct();
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        Wishlist currentWishlist;
        if (wishlist.isEmpty()) {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(userRepository.findById(userId).orElse(null));
            wishlistRepository.save(newWishlist);
            wishlistProduct.setWishlist(newWishlist);
            currentWishlist = newWishlist;
        }
        else {
            wishlistProduct.setWishlist(wishlist.get());
            currentWishlist = wishlist.get();
        }
        if (wishlistProductRepository.findByWishlistIdAndProductId(currentWishlist.getId(), productId) != null) {
            return false;
        }
        wishlistProduct.setProduct(product);
        wishlistProductRepository.save(wishlistProduct);
        return true;
    }

    public boolean removeProductFromWishlist(Long userId, Long productId) {
        Optional<Wishlist> wishlist = wishlistRepository.findByUserId(userId);
        if (wishlist.isEmpty()) {
            return false;
        }
        WishlistProduct wishlistProduct = wishlistProductRepository.findByWishlistIdAndProductId(wishlist.get().getId(), productId);
        if (wishlistProduct == null) {
            return false;
        }
        wishlistProductRepository.delete(wishlistProduct);
        return true;
    }
}
