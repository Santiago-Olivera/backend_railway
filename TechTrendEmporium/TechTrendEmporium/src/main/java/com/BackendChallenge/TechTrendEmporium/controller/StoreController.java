package com.BackendChallenge.TechTrendEmporium.controller;

import com.BackendChallenge.TechTrendEmporium.Requests.AddReviewRequest;
import com.BackendChallenge.TechTrendEmporium.dto.ProductDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.Review;
import com.BackendChallenge.TechTrendEmporium.service.ProductService;
import com.BackendChallenge.TechTrendEmporium.Response.ReviewResponse;
import com.BackendChallenge.TechTrendEmporium.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/products")
public class StoreController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping(value = "{product_id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("product_id") Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            ProductDTO productDTO = productService.convertToDTO(product);
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping(value = "{product_id}/reviews/add")
    public ResponseEntity<String> addReviewToProduct(@PathVariable("product_id") Long productId, @RequestBody AddReviewRequest request) {
        Review newReview = reviewService.addReviewToProduct(request.getUser(), productId, request.getComment(), request.getRating());
        if (newReview != null) {
            return ResponseEntity.ok("Review added");
        } else {
            return ResponseEntity.ok("User or product not found. Review not added.");
        }
    }

    @GetMapping(value = "{product_id}/reviews")
    public ResponseEntity<?> getReviewsForProduct(@PathVariable("product_id") Long productId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByProduct(productId);
        if (reviews == null || reviews.isEmpty()) {
            return new ResponseEntity<>("No reviews for this product", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
    }
}
