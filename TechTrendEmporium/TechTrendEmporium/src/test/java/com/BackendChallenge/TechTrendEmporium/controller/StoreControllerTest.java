package com.BackendChallenge.TechTrendEmporium.controller;
import com.BackendChallenge.TechTrendEmporium.Requests.AddReviewRequest;
import com.BackendChallenge.TechTrendEmporium.dto.ProductDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.Review;
import com.BackendChallenge.TechTrendEmporium.service.ProductService;
import com.BackendChallenge.TechTrendEmporium.Response.ReviewResponse;
import com.BackendChallenge.TechTrendEmporium.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private StoreController storeController;

    @Test
    public void getProductByIdTest_Success() {
        Product product = new Product();
        product.setId(1L);
        when(productService.getProductById(any())).thenReturn(product);
        ResponseEntity<ProductDTO> response = storeController.getProductById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getProductByIdTest_NotFound() {
        when(productService.getProductById(any())).thenReturn(null);
        ResponseEntity<ProductDTO> response = storeController.getProductById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addReviewToProductTest_Success() {
        AddReviewRequest request = new AddReviewRequest();
        request.setUser("testUser");
        request.setComment("testComment");
        request.setRating(5F);
        Review review = new Review();
        when(reviewService.addReviewToProduct(any(), any(), any(), any())).thenReturn(review);
        ResponseEntity<String> response = storeController.addReviewToProduct(1L, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addReviewToProductTest_Failure() {
        AddReviewRequest request = new AddReviewRequest();
        request.setUser("testUser");
        request.setComment("testComment");
        request.setRating(5F);
        when(reviewService.addReviewToProduct(any(), any(), any(), any())).thenReturn(null);
        ResponseEntity<String> response = storeController.addReviewToProduct(1L, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getReviewsForProductTest_Success() {
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setUser("testUser");
        reviewResponse.setComment("testComment");
        reviewResponse.setRating(5F);
        when(reviewService.getReviewsByProduct(any())).thenReturn(Collections.singletonList(reviewResponse));
        ResponseEntity<?> response = storeController.getReviewsForProduct(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getReviewsForProductTest_NoReviews() {
        when(reviewService.getReviewsByProduct(any())).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = storeController.getReviewsForProduct(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}