package com.BackendChallenge.TechTrendEmporium.service;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.Review;
import com.BackendChallenge.TechTrendEmporium.entity.User;
import com.BackendChallenge.TechTrendEmporium.repository.ProductRepository;
import com.BackendChallenge.TechTrendEmporium.repository.ReviewRepository;
import com.BackendChallenge.TechTrendEmporium.repository.UserRepository;
import com.BackendChallenge.TechTrendEmporium.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addReviewToProductTest() {
        User user = new User();
        user.setUsername("username");
        Product product = new Product();
        Product.Rating rating = new Product.Rating();
        rating.setCount(0);
        rating.setRate(0.0);
        product.setRating(rating);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Review review = reviewService.addReviewToProduct("username", 1L, "review", 5.0f);

        assertEquals("username", review.getUser().getUsername());
        assertEquals("review", review.getComment());
        assertEquals(5.0f, review.getRating());
    }

    @Test
    public void getReviewsByProductTest() {
        Object[] reviewData = new Object[]{"username", 1L, "review", 5.0f};
        List<Object[]> reviews = new ArrayList<>();
        reviews.add(reviewData);

        when(reviewRepository.findReviewsByProductId(anyLong())).thenReturn(reviews);

        var reviewResponses = reviewService.getReviewsByProduct(1L);

        assertEquals(1, reviewResponses.size());
        assertEquals("username", reviewResponses.get(0).getUser());
        assertEquals(1L, reviewResponses.get(0).getProductId());
        assertEquals("review", reviewResponses.get(0).getComment());
        assertEquals(5.0f, reviewResponses.get(0).getRating());
    }
}