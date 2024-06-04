package com.BackendChallenge.TechTrendEmporium.controller;
import com.BackendChallenge.TechTrendEmporium.Requests.WishlistRequest;
import com.BackendChallenge.TechTrendEmporium.Response.WishlistResponse;
import com.BackendChallenge.TechTrendEmporium.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    @Test
    public void getWishlistTest_Success() {
        WishlistRequest request = new WishlistRequest();
        request.setUser_id(1L);
        WishlistResponse wishlistResponse = new WishlistResponse();
        when(wishlistService.getWishlistByUser(any())).thenReturn(wishlistResponse);
        ResponseEntity<?> response = wishlistController.getWishlist(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getWishlistTest_Empty() {
        WishlistRequest request = new WishlistRequest();
        request.setUser_id(1L);
        when(wishlistService.getWishlistByUser(any())).thenReturn(null);
        ResponseEntity<?> response = wishlistController.getWishlist(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addProductToWishlistTest_Success() {
        WishlistRequest request = new WishlistRequest();
        request.setUser_id(1L);
        when(wishlistService.addProductToWishlist(any(), any())).thenReturn(true);
        ResponseEntity<String> response = wishlistController.addProductToWishlist(request, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addProductToWishlistTest_Failure() {
        WishlistRequest request = new WishlistRequest();
        request.setUser_id(1L);
        when(wishlistService.addProductToWishlist(any(), any())).thenReturn(false);
        ResponseEntity<String> response = wishlistController.addProductToWishlist(request, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void removeProductFromWishlistTest_Success() {
        WishlistRequest request = new WishlistRequest();
        request.setUser_id(1L);
        when(wishlistService.removeProductFromWishlist(any(), any())).thenReturn(true);
        ResponseEntity<String> response = wishlistController.removeProductFromWishlist(request, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void removeProductFromWishlistTest_Failure() {
        WishlistRequest request = new WishlistRequest();
        request.setUser_id(1L);
        when(wishlistService.removeProductFromWishlist(any(), any())).thenReturn(false);
        ResponseEntity<String> response = wishlistController.removeProductFromWishlist(request, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}