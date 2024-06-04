package com.BackendChallenge.TechTrendEmporium.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.BackendChallenge.TechTrendEmporium.dto.ProductDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.ProductUpdateRequest;
import com.BackendChallenge.TechTrendEmporium.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    @Test
    void testGetProducts() {
        // Define test parameters
        String category = "electronics";
        String price = "desc";
        String title = null;
        int page = 0;
        int size = 6;

        // Mock behavior of productService methods
        lenient().when(productService.getAllProductsSortedByPrice(price)).thenReturn(null);
        lenient().when(productService.getAllProducts()).thenReturn(null);

        // Call the method under test
        ResponseEntity<List<ProductDTO>> responseEntity = productController.getProducts(category, price, title, page, size);

        // Assert the response status
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());


        // Verify interactions with productService methods
        verify(productService, times(1)).getAllProductsSortedByPrice(price);
        verify(productService, never()).getAllProductsSortedByTitle(anyString());
        verify(productService, never()).getProductsByCategory(anyString());
        verify(productService, never()).getAllProducts();
        verify(productService, never()).filterApprovedProducts(anyList());
        verify(productService, never()).getPaginatedList(anyList(), anyInt(), anyInt());
        verify(productService, never()).convertToDTO(any(Product.class));
    }
    @Test
    void testGetProducts_CategoryNotNull_ProductsAvailable() {
        // Define test parameters
        String category = "electronics"; // Assuming a specific category
        String price = null;
        String title = null;
        int page = 0;
        int size = 6;

        // Mock behavior of productService methods
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product()); // Adding a mock product
        lenient().when(productService.getProductsByCategory(category)).thenReturn(mockProducts);

        // Call the method under test
        ResponseEntity<List<ProductDTO>> responseEntity = productController.getProducts(category, price, title, page, size);

        // Assert the response status
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        // Verify interactions with productService methods
        verify(productService, never()).getAllProductsSortedByPrice(anyString());
        verify(productService, never()).getAllProductsSortedByTitle(anyString());
        verify(productService, times(1)).getProductsByCategory(category);
        verify(productService, never()).getAllProducts();
        verify(productService, times(1)).filterApprovedProducts(mockProducts);

    }





    @Test
    void testCreateProduct_Admin() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mock the behavior of authentication.getAuthorities()
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Product product = new Product();
        product.setTitle("Test Product");

        // Mock created product
        Product createdProduct = new Product();
        createdProduct.setId(1L);

        // Mock productService behavior
        when(productService.createProduct(product)).thenReturn(createdProduct);

        // Call the method under test
        ResponseEntity<?> responseEntity = productController.createProduct(product, authentication);

        // Verify that productService.createProduct(product) is called
        verify(productService, times(1)).createProduct(product);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Product created successfully", responseBody.get("message"));
        assertEquals(1L, responseBody.get("productId"));
    }
    @Test
    void testCreateProduct_Employee() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mock the behavior of authentication.getAuthorities()
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("EMPLOYEE"));
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Product product = new Product();
        product.setTitle("Test Product");

        // Mock created product
        Product createdProduct = new Product();
        createdProduct.setId(1L);

        // Mock productService behavior
        when(productService.createProduct(product)).thenReturn(createdProduct);

        // Call the method under test
        ResponseEntity<?> responseEntity = productController.createProduct(product, authentication);

        // Verify that productService.createProduct(product) is called
        verify(productService, times(1)).createProduct(product);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Product creation pending approval", responseBody.get("message"));
        assertEquals(1L, responseBody.get("productId"));
    }
    @Test
    void testCreateProduct_AccessDenied() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mock the behavior of authentication.getAuthorities()
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Product product = new Product();
        product.setTitle("Test Product");

        // Call the method under test
        ResponseEntity<?> responseEntity = productController.createProduct(product, authentication);

        // Verify that productService.createProduct(product) is not called
        verify(productService, never()).createProduct(any(Product.class));

        // Assert the response status and body
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.FORBIDDEN.value(), responseBody.get("status"));
        assertEquals(HttpStatus.FORBIDDEN.getReasonPhrase(), responseBody.get("error"));
        assertEquals("Access Denied", responseBody.get("message"));
    }
    @Test
    void testDeleteProduct_Admin() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mock the behavior of authentication.getAuthorities()
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Map<String, Long> requestBody = Collections.singletonMap("id", 123L);

        // Call the method under test
        ResponseEntity<?> responseEntity = productController.deleteProduct(requestBody, authentication);

        // Verify that productService.deleteProduct(productId) is called
        verify(productService, times(1)).deleteProduct(123L);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Deleted successfully", responseBody.get("message"));
    }
    @Test
    void testDeleteProduct_Employee() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mock the behavior of authentication.getAuthorities()
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("EMPLOYEE"));
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Map<String, Long> requestBody = Collections.singletonMap("id", 123L);

        // Call the method under test
        ResponseEntity<?> responseEntity = productController.deleteProduct(requestBody, authentication);

        // Verify that productService.pendingDeleteProduct(productId) is called
        verify(productService, times(1)).pendingDeleteProduct(123L);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Delete pending for approval", responseBody.get("message"));
    }
    @Test
    void testDeleteProduct_AccessDenied() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mock the behavior of authentication.getAuthorities()
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Map<String, Long> requestBody = Collections.singletonMap("id", 123L);

        // Call the method under test
        ResponseEntity<?> responseEntity = productController.deleteProduct(requestBody, authentication);

        // Verify that productService.deleteProduct(productId) or productService.pendingDeleteProduct(productId) is not called
        verify(productService, never()).deleteProduct(anyLong());
        verify(productService, never()).pendingDeleteProduct(anyLong());

        // Assert the response status and body
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Access Denied", responseEntity.getBody());
    }
    @Test
    void testUpdateProduct_Success() {
        // Mock request body
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setId(123L);
        request.setTitle("Updated Title");

        // Call the method under test
        ResponseEntity<?> responseEntity = productController.updateProduct(request);

        // Verify that productService.updateProduct(request) is called
        verify(productService, times(1)).updateProduct(request);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Updated successfully", responseBody.get("message"));
    }
    @Test
    void testUpdateProduct_NotFound() {
        // Mock request body
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setId(123L);
        request.setTitle("Updated Title");

        // Mock productService.updateProduct(request) to throw NoSuchElementException
        doThrow(NoSuchElementException.class).when(productService).updateProduct(request);

        // Call the method under test
        ResponseEntity<?> responseEntity = productController.updateProduct(request);

        // Verify that productService.updateProduct(request) is called
        verify(productService, times(1)).updateProduct(request);

        // Assert the response status
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}


