package com.BackendChallenge.TechTrendEmporium.controller;

import com.BackendChallenge.TechTrendEmporium.dto.CategoryDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Category;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryUpdateRequest;
import com.BackendChallenge.TechTrendEmporium.service.CategoryService;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void testGetAllCategories() {
        // Mock data
        List<CategoryDTO> mockCategories = Arrays.asList(
                new CategoryDTO(1L, "Category 1"),
                new CategoryDTO(2L, "Category 2")
        );

        // Mock the behavior of categoryService.getApprovedCategories
        when(categoryService.getApprovedCategories()).thenReturn(mockCategories);

        // Call the method under test
        List<CategoryDTO> result = categoryController.getAllCategories();

        // Verify that categoryService.getApprovedCategories() is called
        verify(categoryService, times(1)).getApprovedCategories();

        // Assert the result
        assertNotNull(result);
        assertEquals(2, result.size());
        // Add more assertions as needed based on the expected behavior
    }
    @Test
    void testCreateCategory_Admin() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mock the behavior of authentication.getAuthorities()
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Category category = new Category();
        category.setName("TestCategory");

        // Call the method under test
        ResponseEntity<?> responseEntity = categoryController.createCategory(category, authentication);

        // Verify that categoryService.createCategory(category) is called
        verify(categoryService, times(1)).createCategory(category);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Category created successfully", responseEntity.getBody());
    }
    @Test
    void testCreateCategory_Employee() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);

        // Create a collection with an EMPLOYEE authority
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("EMPLOYEE"));

        // Set up the behavior of getAuthorities() to return the collection
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Category category = new Category();
        category.setName("TestCategory");

        // Call the method under test
        ResponseEntity<?> responseEntity = categoryController.createCategory(category, authentication);

        // Verify that categoryService.createCategory(category) is called
        verify(categoryService, times(1)).createCategory(category);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Category creation pending approval", responseEntity.getBody());
    }
    @Test
    void testCreateCategory_AccessDenied() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);

        // Create a set with a USER authority
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        // Set up the behavior of getAuthorities() to return the set
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Category category = new Category();
        category.setName("TestCategory");

        // Call the method under test
        ResponseEntity<?> responseEntity = categoryController.createCategory(category, authentication);

        // Verify that categoryService.createCategory(category) is not called
        verify(categoryService, never()).createCategory(any(Category.class));

        // Assert the response
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Access Denied", responseEntity.getBody());
    }
    @Test
    void testDeleteCategory_Admin() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);

        // Mock the behavior of authentication.getAuthorities()
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("id", 123L);

        // Call the method under test
        ResponseEntity<?> responseEntity = categoryController.deleteCategory(requestBody, authentication);

        // Verify that categoryService.deleteCategory(categoryId) is called
        verify(categoryService, times(1)).deleteCategory(123L);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Category deleted successfully", responseEntity.getBody());
    }
    @Test
    void testDeleteCategory_Employee() {
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);

        // Mock the behavior of authentication.getAuthorities()
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("EMPLOYEE"));
        //noinspection unchecked
        when(((Collection<GrantedAuthority>) authentication.getAuthorities())).thenReturn(authorities);

        // Mock request body
        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("id", 123L);

        // Call the method under test
        ResponseEntity<?> responseEntity = categoryController.deleteCategory(requestBody, authentication);

        // Verify that categoryService.requestCategoryDeletion(categoryId) is called
        verify(categoryService, times(1)).requestCategoryDeletion(123L);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Category deletion request pending approval", responseEntity.getBody());
    }

    @Test
    void testUpdateCategory() {
        // Mock request body
        CategoryUpdateRequest request = new CategoryUpdateRequest();
        request.setId(123L);
        request.setName("UpdatedCategory");

        // Call the method under test
        ResponseEntity<?> responseEntity = categoryController.updateCategory(request);

        // Verify that categoryService.updateCategory(request) is called
        verify(categoryService, times(1)).updateCategory(request);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        //noinspection unchecked
        assertEquals("Updated successfully",
                ((Map<String, String>) Objects.requireNonNull(responseEntity.getBody())).get("message"));
    }

    @Test
    void testUpdateCategory_NotFound() {
        // Mock request body
        CategoryUpdateRequest request = new CategoryUpdateRequest();
        request.setId(1L);
        request.setName("UpdatedCategory");

        // Mock the behavior of categoryService.updateCategory(request) to throw NoSuchElementException
        doThrow(new NoSuchElementException()).when(categoryService).updateCategory(request);

        // Call the method under test
        ResponseEntity<?> responseEntity = categoryController.updateCategory(request);

        // Verify that categoryService.updateCategory(request) is called
        verify(categoryService, times(1)).updateCategory(request);

        // Assert the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}

