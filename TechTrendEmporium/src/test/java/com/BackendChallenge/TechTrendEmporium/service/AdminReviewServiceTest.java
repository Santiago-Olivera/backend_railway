package com.BackendChallenge.TechTrendEmporium.service;

import com.BackendChallenge.TechTrendEmporium.dto.JobDTO;
import com.BackendChallenge.TechTrendEmporium.dto.ReviewJobRequestDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Category;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryStatus;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.ProductStatus;
import com.BackendChallenge.TechTrendEmporium.repository.CategoryRepository;
import com.BackendChallenge.TechTrendEmporium.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminReviewServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AdminReviewService adminReviewService;
    @Mock
    private AdminReviewService adminReviewServiceMock; // Mock instance

    @InjectMocks
    private AdminReviewService adminReviewServiceUnderTest;


    @Test
    public void testGetPendingApprovalJobs() {
        // Prepare test data
        Product product1 = new Product();
        product1.setId(1L);
        product1.setStatus(ProductStatus.PENDING_CREATION);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setStatus(ProductStatus.PENDING_DELETE);

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setStatus(CategoryStatus.PENDING);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setStatus(CategoryStatus.PENDING_DELETE);

        // Stubbing behavior of productRepository.findByStatus()
        when(productRepository.findByStatus(ProductStatus.PENDING_CREATION)).thenReturn(Arrays.asList(product1));
        when(productRepository.findByStatus(ProductStatus.PENDING_DELETE)).thenReturn(Arrays.asList(product2));

        // Stubbing behavior of categoryRepository.findByStatus()
        when(categoryRepository.findByStatus(CategoryStatus.PENDING)).thenReturn(Arrays.asList(category1));
        when(categoryRepository.findByStatus(CategoryStatus.PENDING_DELETE)).thenReturn(Arrays.asList(category2));

        // Call the method under test
        List<JobDTO> result = adminReviewService.getPendingApprovalJobs();

        // Verify the result
        List<JobDTO> expectedJobs = Arrays.asList(
                new JobDTO("product", 1L, "create"),
                new JobDTO("category", 1L, "create"),
                new JobDTO("product", 2L, "delete"),
                new JobDTO("category", 2L, "delete")
        );
        assertEquals(expectedJobs, result);
    }
    @Test
    public void testApproveProductCreation() {
        // Prepare test data
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setStatus(ProductStatus.PENDING_CREATION);

        // Stubbing behavior of productRepository.findById()
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call the method under test
        adminReviewService.approveProductCreation(productId);

        // Verify that productRepository.findById() is called with the correct productId
        verify(productRepository).findById(productId);

        // Verify that productRepository.save() is called with the updated product
        verify(productRepository).save(product);
    }
    @Test
    public void testDenyProductCreation() {
        // Prepare test data
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setStatus(ProductStatus.PENDING_CREATION);

        // Stubbing behavior of productRepository.findById()
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call the method under test
        adminReviewService.denyProductCreation(productId);

        // Verify that productRepository.findById() is called with the correct productId
        verify(productRepository).findById(productId);

        // Verify that productRepository.save() is called with the updated product
        verify(productRepository).save(product);
    }
    @Test
    public void testApproveProductDeletion() {
        // Prepare test data
        Long productId = 1L;

        // Call the method under test
        adminReviewService.approveProductDeletion(productId);

        // Verify that productRepository.deleteById() is called with the correct productId
        verify(productRepository).deleteById(productId);
    }
    @Test
    public void testDenyProductDeletion() {
        // Prepare test data
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setStatus(ProductStatus.PENDING_DELETE);

        // Stubbing behavior of productRepository.findById()
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call the method under test
        adminReviewService.denyProductDeletion(productId);

        // Verify that productRepository.findById() is called with the correct productId
        verify(productRepository).findById(productId);

        // Verify that productRepository.save() is called with the updated product
        verify(productRepository).save(product);

        // Assert that the status of the product is now APPROVED
        assertEquals(ProductStatus.APPROVED, product.getStatus());
    }
    @Test
    public void testApproveCategory() {
        // Prepare test data
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setStatus(CategoryStatus.PENDING);

        // Stubbing behavior of categoryRepository.findById()
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Call the method under test
        adminReviewService.approveCategory(categoryId);

        // Verify that categoryRepository.findById() is called with the correct categoryId
        verify(categoryRepository).findById(categoryId);

        // Verify that categoryRepository.save() is called with the updated category
        verify(categoryRepository).save(category);

        // Assert that the status of the category is now APPROVED
        assertEquals(CategoryStatus.APPROVED, category.getStatus());
    }
    @Test
    public void testDenyCategory() {
        // Prepare test data
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setStatus(CategoryStatus.PENDING);

        // Stubbing behavior of categoryRepository.findById()
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Call the method under test
        adminReviewService.denyCategory(categoryId);

        // Verify that categoryRepository.findById() is called with the correct categoryId
        verify(categoryRepository).findById(categoryId);

        // Verify that categoryRepository.delete() is called with the category
        verify(categoryRepository).delete(category);

        // Assert that the status of the category is now DENIED
        assertEquals(CategoryStatus.DENIED, category.getStatus());
    }
    @Test
    public void testApproveCategoryDeletion() {
        // Prepare test data
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);

        // Stubbing behavior of categoryRepository.findById()
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Call the method under test
        adminReviewService.approveCategoryDeletion(categoryId);

        // Verify that categoryRepository.findById() is called with the correct categoryId
        verify(categoryRepository).findById(categoryId);

        // Verify that categoryRepository.delete() is called with the category
        verify(categoryRepository).delete(category);
    }
    @Test
    public void testDenyCategoryDeletion() {
        // Prepare test data
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setStatus(CategoryStatus.PENDING_DELETE);

        // Stubbing behavior of categoryRepository.findById()
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Call the method under test
        adminReviewService.denyCategoryDeletion(categoryId);

        // Verify that categoryRepository.findById() is called with the correct categoryId
        verify(categoryRepository).findById(categoryId);

        // Verify that categoryRepository.save() is called with the updated category
        verify(categoryRepository).save(category);

        // Assert that the status of the category is now APPROVED
        assertEquals(CategoryStatus.APPROVED, category.getStatus());
    }
    @Test
    public void testReviewJob_Product_Create_Decline() {
        /* in this test reviewJob is invoked with parameters indicating a product creation request to be declined,
        and we want to verify the method behaves as expected*/

        // Create ReviewJobRequestDTO object for product creation denial
        ReviewJobRequestDTO requestDTO = new ReviewJobRequestDTO(1L, "create", "decline");

        // Call the method under test
        adminReviewServiceUnderTest.reviewJob("product", requestDTO);

        // Verify that no other methods are called on the mock
        verify(adminReviewServiceMock, never()).approveProductCreation(1L);
        verify(adminReviewServiceMock, never()).approveProductDeletion(1L);
        verify(adminReviewServiceMock, never()).denyProductDeletion(1L);
        verify(adminReviewServiceMock, never()).approveCategory(1L);
        verify(adminReviewServiceMock, never()).denyCategory(1L);
        verify(adminReviewServiceMock, never()).approveCategoryDeletion(1L);
        verify(adminReviewServiceMock, never()).denyCategoryDeletion(1L);
    }
    @Test
    public void testReviewJob_Product_Create_Approve() {
        //Test for Approving Product Creation:
        // Create ReviewJobRequestDTO object for product creation approval
        ReviewJobRequestDTO requestDTO = new ReviewJobRequestDTO(1L, "create", "approve");

        // Call the method under test
        adminReviewServiceUnderTest.reviewJob("product", requestDTO);

        // Verify that no other methods are called on the mock
        verify(adminReviewServiceMock, never()).denyProductCreation(1L);
        verify(adminReviewServiceMock, never()).approveProductDeletion(1L);
        verify(adminReviewServiceMock, never()).denyProductDeletion(1L);
        verify(adminReviewServiceMock, never()).approveCategory(1L);
        verify(adminReviewServiceMock, never()).denyCategory(1L);
        verify(adminReviewServiceMock, never()).approveCategoryDeletion(1L);
        verify(adminReviewServiceMock, never()).denyCategoryDeletion(1L);
    }
    @Test
    public void testReviewJob_Category_Delete_Decline() {
        // Mocking a Category entity with ID 1L for test purposes
        Category category = new Category();
        category.setId(1L);

        // Set up behavior for findById in categoryRepository mock
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        // Create ReviewJobRequestDTO object for category deletion denial
        ReviewJobRequestDTO requestDTO = new ReviewJobRequestDTO(1L, "delete", "decline");

        // Call the method under test
        adminReviewServiceUnderTest.reviewJob("category", requestDTO);

        // Verify that no other methods are called on the mock
        verify(adminReviewServiceMock, never()).approveProductCreation(1L);
        verify(adminReviewServiceMock, never()).approveProductDeletion(1L);
        verify(adminReviewServiceMock, never()).denyProductDeletion(1L);
        verify(adminReviewServiceMock, never()).approveCategory(1L);
        verify(adminReviewServiceMock, never()).denyCategory(1L);
        verify(adminReviewServiceMock, never()).approveCategoryDeletion(1L);
    }



}

