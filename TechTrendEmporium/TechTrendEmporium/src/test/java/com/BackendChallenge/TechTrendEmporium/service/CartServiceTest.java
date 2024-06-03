package com.BackendChallenge.TechTrendEmporium.service;

import com.BackendChallenge.TechTrendEmporium.Response.CartResponse;
import com.BackendChallenge.TechTrendEmporium.Response.CheckoutResponse;
import com.BackendChallenge.TechTrendEmporium.entity.*;
import com.BackendChallenge.TechTrendEmporium.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CartProductRepository cartProductRepository;
    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    public void getCartByUserTest_NotFound() {
        when(cartRepository.findByUserIdAndStatus(any(), any())).thenReturn(Optional.empty());
        assertNull(cartService.getCartByUser(1L));
    }

    @Test
    public void checkoutTest_Failure() {
        when(cartRepository.findByUserIdAndStatus(any(), any())).thenReturn(Optional.empty());
        assertEquals("Checkout failed, cart not found.", cartService.checkout(1L).getMessage());
    }

    @Test
    public void applyCouponTest_Success() {
        when(cartRepository.findByUserIdAndStatus(any(), any())).thenReturn(Optional.of(new Cart()));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(couponRepository.findByName(any())).thenReturn(Optional.of(new Coupon()));
        assertTrue(cartService.applyCoupon(1L, "testCoupon"));
    }

    @Test
    public void applyCouponTest_Failure() {
        when(cartRepository.findByUserIdAndStatus(any(), any())).thenReturn(Optional.empty());
        assertFalse(cartService.applyCoupon(1L, "testCoupon"));
    }

    @Test
    public void addProductToCartTest_CartExists_ProductExists_QuantityAvailable() {
        // Prepare the data
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 1;

        Cart cart = new Cart();
        cart.setId(1L); // Set the ID
        cart.setUser(new User()); // Set the User
        cart.setStatus("OPEN"); // Set the Status
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        Product product = new Product();
        product.setId(1L); // Set the ID
        product.setInventory(new Product.Inventory());
        product.getInventory().setAvailable(quantity);

        // Mock the repository calls
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartIdAndProductId(cart.getId(), productId)).thenReturn(null);
        when(cartProductRepository.save(any(CartProduct.class))).thenAnswer(invocation -> {
            CartProduct savedCartProduct = invocation.getArgument(0);
            savedCartProduct.setId(1L); // Set the ID of the saved CartProduct
            return savedCartProduct;
        });

        // Call the method and check the result
        boolean result = cartService.addProductToCart(userId, productId, quantity);
        assertTrue(result);

        // Verify the interactions
        verify(cartProductRepository, times(1)).save(any(CartProduct.class));
    }

    @Test
    public void addProductToCartTest_CartDoesNotExist() {
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 1;

        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.empty());

        boolean result = cartService.addProductToCart(userId, productId, quantity);
        assertFalse(result);
    }

    @Test
    public void addProductToCartTest_ProductDoesNotExist() {
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 1;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(new User());
        cart.setStatus("OPEN");
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        boolean result = cartService.addProductToCart(userId, productId, quantity);
        assertFalse(result);
    }

    @Test
    public void addProductToCartTest_ProductQuantityNotAvailable() {
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(new User());
        cart.setStatus("OPEN");
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        Product product = new Product();
        product.setId(1L);
        product.setInventory(new Product.Inventory());
        product.getInventory().setAvailable(quantity - 1);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        boolean result = cartService.addProductToCart(userId, productId, quantity);
        assertFalse(result);
    }

    @Test
    public void addProductToCartTest_CartExists_ProductExists_QuantityAvailable_CartProductExists() {
        // Prepare the data
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 1;

        Cart cart = new Cart();
        cart.setId(1L); // Set the ID
        cart.setUser(new User()); // Set the User
        cart.setStatus("OPEN"); // Set the Status
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        Product product = new Product();
        product.setId(1L); // Set the ID
        product.setInventory(new Product.Inventory());
        product.getInventory().setAvailable(quantity + 1); // Set available quantity to be more than the requested quantity

        CartProduct existingCartProduct = new CartProduct();
        existingCartProduct.setId(1L); // Set the ID
        existingCartProduct.setCart(cart); // Set the Cart
        existingCartProduct.setProduct(product); // Set the Product
        existingCartProduct.setQuantity(quantity); // Set the Quantity

        // Mock the repository calls
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartIdAndProductId(cart.getId(), productId)).thenReturn(existingCartProduct);
        doNothing().when(cartProductRepository).updateQuantity(anyInt(), anyLong());

        // Call the method and check the result
        boolean result = cartService.addProductToCart(userId, productId, quantity);
        assertTrue(result);

        // Verify the interactions
        verify(cartProductRepository, times(1)).updateQuantity(existingCartProduct.getQuantity() + quantity, existingCartProduct.getId());
    }

    @Test
    public void deleteProductFromCartTest_CartDoesNotExist() {
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 1;

        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.empty());

        boolean result = cartService.deleteProductFromCart(userId, productId, quantity);
        assertFalse(result);
    }

    @Test
    public void deleteProductFromCartTest_CartProductDoesNotExist() {
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 1;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(new User());
        cart.setStatus("OPEN");
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        when(cartProductRepository.findByCartIdAndProductId(cart.getId(), productId)).thenReturn(null);

        boolean result = cartService.deleteProductFromCart(userId, productId, quantity);
        assertFalse(result);
    }

    @Test
    public void deleteProductFromCartTest_CartProductQuantityLessThanOrEqualToRequested() {
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 1;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(new User());
        cart.setStatus("OPEN");
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(1L);
        cartProduct.setCart(cart);
        cartProduct.setProduct(new Product());
        cartProduct.setQuantity(quantity);
        when(cartProductRepository.findByCartIdAndProductId(cart.getId(), productId)).thenReturn(cartProduct);

        doNothing().when(cartProductRepository).delete(cartProduct);

        boolean result = cartService.deleteProductFromCart(userId, productId, quantity);
        assertTrue(result);

        verify(cartProductRepository, times(1)).delete(cartProduct);
    }

    @Test
    public void getCartByUserTest_CartDoesNotExist() {
        Long userId = 1L;

        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.empty());

        CartResponse result = cartService.getCartByUser(userId);
        assertNull(result);
    }

    @Test
    public void getCartByUserTest_CartExists_NoCartProduct() {
        Long userId = 1L;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(new User());
        cart.setStatus("OPEN");
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        when(cartProductRepository.findByCartId(cart.getId())).thenReturn(new ArrayList<>());

        CartResponse result = cartService.getCartByUser(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUser_id());
        assertTrue(result.getProducts().isEmpty());
    }

    @Test
    public void getCartByUserTest_CartExists_WithCartProduct() {
        Long userId = 1L;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(new User());
        cart.setStatus("OPEN");
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(1L);
        cartProduct.setCart(cart);
        cartProduct.setProduct(new Product());
        cartProduct.setQuantity(1);
        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(cartProduct);
        when(cartProductRepository.findByCartId(cart.getId())).thenReturn(cartProducts);

        CartResponse result = cartService.getCartByUser(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUser_id());
        assertFalse(result.getProducts().isEmpty());
    }

    @Test
    public void checkoutTest_CartDoesNotExist() {
        Long userId = 1L;

        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.empty());

        CheckoutResponse result = cartService.checkout(userId);
        assertNotNull(result);
        assertEquals("Checkout failed, cart not found.", result.getMessage());
    }

    @Test
    public void checkoutTest_Success() {
        Long userId = 1L;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(new User());
        cart.setStatus("OPEN");
        Coupon coupon = new Coupon(); // Create a new Coupon
        coupon.setDiscountPercentage(10); // Set a discount percentage
        cart.setCoupon(coupon); // Set the Coupon in the Cart
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(1L);
        cartProduct.setCart(cart);
        cartProduct.setProduct(new Product());
        cartProduct.setQuantity(1);
        cartProduct.getProduct().setInventory(new Product.Inventory());
        cartProduct.getProduct().getInventory().setAvailable(1);
        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(cartProduct);
        when(cartProductRepository.findByCartId(cart.getId())).thenReturn(cartProducts);

        when(productRepository.save(any(Product.class))).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenReturn(null);
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CheckoutResponse result = cartService.checkout(userId);
        assertNotNull(result);
        assertEquals("Checkout successful", result.getMessage());
    }

    @Test
    public void checkoutTest_NotEnoughInventory() {
        Long userId = 1L;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(new User());
        cart.setStatus("OPEN");
        Coupon coupon = new Coupon(); // Create a new Coupon
        coupon.setDiscountPercentage(10); // Set a discount percentage
        cart.setCoupon(coupon); // Set the Coupon in the Cart
        when(cartRepository.findByUserIdAndStatus(userId, "OPEN")).thenReturn(Optional.of(cart));

        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(1L);
        cartProduct.setCart(cart);
        cartProduct.setProduct(new Product());
        cartProduct.setQuantity(2); // Set quantity greater than available inventory
        cartProduct.getProduct().setInventory(new Product.Inventory());
        cartProduct.getProduct().getInventory().setAvailable(1); // Set available inventory less than quantity
        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(cartProduct);
        when(cartProductRepository.findByCartId(cart.getId())).thenReturn(cartProducts);

        CheckoutResponse result = cartService.checkout(userId);
        assertNotNull(result);
        assertEquals("Checkout failed, not enough stock.", result.getMessage());
    }

}