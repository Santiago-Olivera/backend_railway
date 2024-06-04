package com.BackendChallenge.TechTrendEmporium.service;

import com.BackendChallenge.TechTrendEmporium.dto.CategoryDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Category;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryStatus;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryUpdateRequest;
import com.BackendChallenge.TechTrendEmporium.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testFetchCategoryNames() {
        // Mock external API response
        String[] categoryArray = {"electronics", "jewelery", "men's clothing","women's clothing"};
        lenient().when(restTemplate.getForObject("https://fakestoreapi.com/products/categories", String[].class))
                .thenReturn(categoryArray);

        // Call the method under test
        categoryService.fetchCategoryNames();

        // Verify that saveAll() method was called with correct arguments
        Mockito.verify(categoryRepository).saveAll(Mockito.anyList());

        // Capture the saved categories to verify later
        List<Category> capturedCategories = captureSavedCategories();

        // Mock the behavior of categoryRepository.findAll() to return the saved categories
        when(categoryRepository.findAll()).thenReturn(capturedCategories);

        // Get saved categories from the repository
        List<Category> retrievedCategories = categoryService.getAllCategories();

        // Verify that correct categories were saved
        assertEquals(categoryArray.length, retrievedCategories.size());
        for (int i = 0; i < categoryArray.length; i++) {
            assertEquals(categoryArray[i], retrievedCategories.get(i).getName());
        }
    }

    // Helper method to capture the categories saved by the service
    private List<Category> captureSavedCategories() {
        ArgumentCaptor<List<Category>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(categoryRepository).saveAll(captor.capture());
        return captor.getValue();
    }

    @Test
    public void testGetAllCategories() {
        // Mock the behavior of categoryRepository.findAll()
        List<Category> categoriesList = Arrays.asList(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categoriesList);

        // Call the method under test
        List<Category> retrievedCategories = categoryService.getAllCategories();

        // Verify that findAll() method was called
        verify(categoryRepository).findAll();

        // Verify that correct categories were returned
        assertEquals(2, retrievedCategories.size());
    }

    @Test
    public void testCreateCategory() {
        // Create a sample Category object
        Category category = new Category();
        category.setName("Electronics");

        // Call the method under test
        categoryService.createCategory(category);

        // Verify that categoryRepository.save() is called with the correct Category object
        verify(categoryRepository).save(category);
    }

    @Test
    public void testRequestCategoryDeletion() {
        // Prepare test data
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setStatus(CategoryStatus.APPROVED); // Initial status

        // Stubbing behavior of categoryRepository.findById()
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Call the method under test
        categoryService.requestCategoryDeletion(categoryId);

        // Verify that categoryRepository.findById() is called with the correct categoryId
        verify(categoryRepository).findById(categoryId);

        // Verify that save() is called on categoryRepository to save the changes
        verify(categoryRepository).save(category); // Verify save() method call
    }
    @Test
    public void testGetApprovedCategories() {
        // Prepare test data
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics");
        category1.setStatus(CategoryStatus.APPROVED);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Clothing");
        category2.setStatus(null);
        List<Category> mockCategories = Arrays.asList(category1, category2);

        // Stubbing behavior of categoryRepository.findByStatusInOrStatusIsNull()
        when(categoryRepository.findByStatusInOrStatusIsNull(Arrays.asList(CategoryStatus.APPROVED, null)))
                .thenReturn(mockCategories);

        // Call the method under test
        List<CategoryDTO> result = categoryService.getApprovedCategories();

        // Verify that categoryRepository.findByStatusInOrStatusIsNull() is called with the correct statuses
        verify(categoryRepository).findByStatusInOrStatusIsNull(Arrays.asList(CategoryStatus.APPROVED, null));

        // Verify the mapping to CategoryDTO
        assertEquals(2, result.size());
        assertEquals(category1.getId(), result.get(0).getId());
        assertEquals(category1.getName(), result.get(0).getName());
        assertEquals(category2.getId(), result.get(1).getId());
        assertEquals(category2.getName(), result.get(1).getName());
    }
    @Test
    public void testDeleteCategory() {
        // Prepare test data
        Long categoryId = 1L;

        // Call the method under test
        categoryService.deleteCategory(categoryId);

        // Verify that categoryRepository.deleteById() is called with the correct categoryId
        verify(categoryRepository).deleteById(categoryId);
    }
    @Test
    public void testUpdateCategory() {
        // Prepare test data
        long categoryId = 1L;
        String updatedName = "Updated Category Name";
        CategoryUpdateRequest request = new CategoryUpdateRequest();
        request.setId(categoryId);
        request.setName(updatedName);

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Original Category Name");

        // Stubbing behavior of categoryRepository.findById()
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        // Call the method under test
        categoryService.updateCategory(request);

        // Verify that categoryRepository.findById() is called with the correct categoryId
        verify(categoryRepository).findById(categoryId);


        // Verify that categoryRepository.save() is called with the updated existingCategory
        verify(categoryRepository).save(existingCategory);
    }

}




