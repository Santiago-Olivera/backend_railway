package com.BackendChallenge.TechTrendEmporium.service;


import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.Review;
import com.BackendChallenge.TechTrendEmporium.entity.User;
import com.BackendChallenge.TechTrendEmporium.repository.ProductRepository;
import com.BackendChallenge.TechTrendEmporium.repository.ReviewRepository;
import com.BackendChallenge.TechTrendEmporium.repository.UserRepository;
import com.BackendChallenge.TechTrendEmporium.Response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Review addReviewToProduct(String username, Long productId, String review, Float rating) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        Product.Rating currentRating = product.getRating();

        int count = currentRating.getCount();
        double rate = currentRating.getRate();
        double newRate = (rate * count + rating) / (count + 1);

        currentRating.setCount(count + 1);
        currentRating.setRate(newRate);
        product.setRating(currentRating);
        productRepository.save(product);

        Review newReview = new Review();
        newReview.setUser(user);
        newReview.setProduct(product);
        newReview.setComment(review);
        newReview.setRating(rating);
        return reviewRepository.save(newReview);
    }

    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        List<Object[]> reviews = reviewRepository.findReviewsByProductId(productId);
        if (reviews == null) {
            return null;
        }
        List<ReviewResponse> response = new ArrayList<>();
        for (Object[] review : reviews) {
            ReviewResponse reviewResponse = new ReviewResponse();
            reviewResponse.setUser((String) review[0]);
            reviewResponse.setProductId((Long) review[1]);
            reviewResponse.setComment((String) review[2]);
            reviewResponse.setRating((Float) review[3]);
            response.add(reviewResponse);
        }
        return response;
    }
}
