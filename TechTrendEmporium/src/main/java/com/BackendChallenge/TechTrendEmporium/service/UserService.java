package com.BackendChallenge.TechTrendEmporium.service;

import com.BackendChallenge.TechTrendEmporium.entity.User;
import com.BackendChallenge.TechTrendEmporium.repository.UserRepository;
import com.BackendChallenge.TechTrendEmporium.entity.Cart;
import com.BackendChallenge.TechTrendEmporium.entity.Wishlist;
import com.BackendChallenge.TechTrendEmporium.entity.Review;
import com.BackendChallenge.TechTrendEmporium.repository.CartRepository;
import com.BackendChallenge.TechTrendEmporium.repository.CartProductRepository;
import com.BackendChallenge.TechTrendEmporium.repository.WishlistRepository;
import com.BackendChallenge.TechTrendEmporium.repository.WishlistProductRepository;
import com.BackendChallenge.TechTrendEmporium.repository.SaleRepository;
import com.BackendChallenge.TechTrendEmporium.repository.ReviewRepository;
import com.BackendChallenge.TechTrendEmporium.Response.GetUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private WishlistProductRepository wishlistProductRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public Boolean deleteUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        Optional<User> DeletedUser = userRepository.findByUsername("deleted_users");
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getRole().toString().equals("ADMIN")) {
                return false;
            }

            if (user.getUsername().equals("deleted_users")) {
                return false;
            }

            List<Cart> carts = cartRepository.findAllByUserId(user.getId());
            for (Cart cart : carts) {
                if (DeletedUser.isPresent()){
                    cart.setUser(DeletedUser.get());
                    cart.setStatus("CLOSED");
                    cartRepository.save(cart);
                }
            }

            Optional<Wishlist> wishlist = wishlistRepository.findByUserId(user.getId());

            if (wishlist.isPresent()) {
                if (DeletedUser.isPresent()){
                    wishlist.get().setUser(null);
                    wishlistRepository.save(wishlist.get());
                }
            }


            List<Review> reviews = reviewRepository.findByUserId(user.getId());
            for (Review review : reviews) {
                if (DeletedUser.isPresent()){
                    review.setUser(DeletedUser.get());
                    reviewRepository.save(review);
                }
            }

            userRepository.delete(user);
            return true;

        }
        return false;
    }

    public Boolean updateUser(String username, String email, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (email != null) {
                user.setEmail(email);
            }
            if (password != null) {
                user.setPassword(passwordEncoder.encode(password));
            }
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<GetUsersResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<GetUsersResponse> response = new ArrayList<>();
        for (User user : users) {
            GetUsersResponse getUsersResponse = GetUsersResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
            response.add(getUsersResponse);
        }
        return response;
    }
}