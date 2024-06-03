package com.BackendChallenge.TechTrendEmporium.service;
import com.BackendChallenge.TechTrendEmporium.entity.*;
import com.BackendChallenge.TechTrendEmporium.repository.CartRepository;
import com.BackendChallenge.TechTrendEmporium.repository.ReviewRepository;
import com.BackendChallenge.TechTrendEmporium.repository.UserRepository;
import com.BackendChallenge.TechTrendEmporium.repository.WishlistRepository;
import com.BackendChallenge.TechTrendEmporium.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void deleteUserTest() {
        // Prepare the data
        String username = "username";
        User user = new User();
        user.setUsername(username);
        user.setRole(Role.SHOPPER);
        User deletedUser = new User();
        deletedUser.setUsername("deleted_users");

        Cart cart = new Cart();
        Wishlist wishlist = new Wishlist();
        Review review = new Review();

        // Mock the repository calls
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("deleted_users")).thenReturn(Optional.of(deletedUser));
        when(cartRepository.findAllByUserId(user.getId())).thenReturn(Arrays.asList(cart));
        when(wishlistRepository.findByUserId(user.getId())).thenReturn(Optional.of(wishlist));
        when(reviewRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(review));

        // Call the method and check the result
        boolean result = userService.deleteUser(username);
        assertTrue(result);

        // Verify the interactions
        verify(cartRepository, times(1)).save(cart);
        verify(wishlistRepository, times(1)).save(wishlist);
        verify(reviewRepository, times(1)).save(review);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void deleteUserTest_UserNotFound() {
        // Prepare the data
        String username = "username";

        // Mock the repository calls
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Call the method and check the result
        boolean result = userService.deleteUser(username);
        assertFalse(result);
    }

    @Test
    public void deleteUserTest_AdminUser() {
        // Prepare the data
        String username = "admin";
        User user = new User();
        user.setUsername(username);
        user.setRole(Role.ADMIN);

        // Mock the repository calls
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Call the method and check the result
        boolean result = userService.deleteUser(username);
        assertFalse(result);
    }

    @Test
    public void deleteUserTest_DeletedUser() {
        // Prepare the data
        String username = "deleted_users";
        User user = new User();
        user.setUsername(username);
        user.setRole(Role.SHOPPER);

        // Mock the repository calls
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Call the method and check the result
        boolean result = userService.deleteUser(username);
        assertFalse(result);
    }

    @Test
    public void getAllUsersTest() {
        User user1 = new User();
        user1.setUsername("username1");
        User user2 = new User();
        user2.setUsername("username2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        assertEquals(2, userService.getAllUsers().size());
    }


//    @Test
//    public void updateUserTest_UserNotFound() {
//        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
//
//        assertFalse(userService.updateUser("username", "newEmail", "newPassword"));
//    }

    @Test
    public void updateUserTest_UserFound() {
        // Prepare the data
        String username = "username";
        String email = "newEmail";
        String password = "newPassword";
        User user = new User();
        user.setUsername(username);

        // Mock the repository calls
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // Call the method and check the result
        boolean result = userService.updateUser(username, email, password);
        assertTrue(result);

        // Verify the interactions
        verify(userRepository, times(1)).save(user);
        assertEquals(email, user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void updateUserTest_UserNotFound() {
        // Prepare the data
        String username = "username";
        String email = "newEmail";
        String password = "newPassword";

        // Mock the repository calls
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Call the method and check the result
        boolean result = userService.updateUser(username, email, password);
        assertFalse(result);
    }
}