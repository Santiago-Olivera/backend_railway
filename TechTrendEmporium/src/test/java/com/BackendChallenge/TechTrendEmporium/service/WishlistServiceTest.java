package com.BackendChallenge.TechTrendEmporium.service;

import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.User;
import com.BackendChallenge.TechTrendEmporium.entity.Wishlist;
import com.BackendChallenge.TechTrendEmporium.entity.WishlistProduct;
import com.BackendChallenge.TechTrendEmporium.repository.ProductRepository;
import com.BackendChallenge.TechTrendEmporium.repository.UserRepository;
import com.BackendChallenge.TechTrendEmporium.repository.WishlistProductRepository;
import com.BackendChallenge.TechTrendEmporium.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WishlistServiceTest {

    @InjectMocks
    private WishlistService wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WishlistProductRepository wishlistProductRepository;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getWishlistByUserTest() {
        Wishlist wishlist = new Wishlist();
        when(wishlistRepository.findByUserId(anyLong())).thenReturn(Optional.of(wishlist));
        when(wishlistProductRepository.findByWishlistId(anyLong())).thenReturn(Arrays.asList(new WishlistProduct()));

        assertNotNull(wishlistService.getWishlistByUser(1L));
    }

    @Test
    public void addProductToWishlistTest() {
        Wishlist wishlist = new Wishlist();
        when(wishlistRepository.findByUserId(anyLong())).thenReturn(Optional.of(wishlist));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(wishlistProductRepository.findByWishlistIdAndProductId(anyLong(), anyLong())).thenReturn(null);

        assertTrue(wishlistService.addProductToWishlist(1L, 1L));
    }

    @Test
    public void removeProductFromWishlistTest() {
        Wishlist wishlist = new Wishlist();
        when(wishlistRepository.findByUserId(anyLong())).thenReturn(Optional.of(wishlist));
        when(wishlistProductRepository.findByWishlistIdAndProductId(anyLong(), anyLong())).thenReturn(new WishlistProduct());

        assertFalse(wishlistService.removeProductFromWishlist(1L, 1L));
    }

    @Test
    public void getWishlistByUserTest_WishlistNotFound() {
        when(wishlistRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        assertNull(wishlistService.getWishlistByUser(1L));
    }

    @Test
    public void addProductToWishlistTest_WishlistProductExists() {
        Wishlist wishlist = new Wishlist();
        when(wishlistRepository.findByUserId(anyLong())).thenReturn(Optional.of(wishlist));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(wishlistProductRepository.findByWishlistIdAndProductId(anyLong(), anyLong())).thenReturn(new WishlistProduct());

        assertTrue(wishlistService.addProductToWishlist(1L, 1L));
    }

    @Test
    public void removeProductFromWishlistTest_WishlistNotFound() {
        when(wishlistRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        assertFalse(wishlistService.removeProductFromWishlist(1L, 1L));
    }

    @Test
    public void removeProductFromWishlistTest_WishlistProductNotFound() {
        Wishlist wishlist = new Wishlist();
        when(wishlistRepository.findByUserId(anyLong())).thenReturn(Optional.of(wishlist));
        when(wishlistProductRepository.findByWishlistIdAndProductId(anyLong(), anyLong())).thenReturn(null);

        assertFalse(wishlistService.removeProductFromWishlist(1L, 1L));
    }

    @Test
    public void addProductToWishlistTest_WishlistIsEmpty() {
        // Prepare the data
        Long userId = 1L;
        Long productId = 1L;

        // Mock the repository calls
        when(wishlistRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));

        // Call the method and check the result
        boolean result = wishlistService.addProductToWishlist(userId, productId);
        assertTrue(result);

        // Verify the interactions
        verify(wishlistRepository, times(1)).save(any(Wishlist.class));
        verify(wishlistProductRepository, times(1)).save(any(WishlistProduct.class));
    }
}