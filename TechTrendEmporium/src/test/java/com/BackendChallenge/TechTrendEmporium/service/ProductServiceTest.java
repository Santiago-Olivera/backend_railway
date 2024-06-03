package com.BackendChallenge.TechTrendEmporium.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.BackendChallenge.TechTrendEmporium.TechTrendEmporiumApplication;
import com.BackendChallenge.TechTrendEmporium.dto.ProductDTO;
import com.BackendChallenge.TechTrendEmporium.entity.ProductStatus;
import com.BackendChallenge.TechTrendEmporium.entity.ProductUpdateRequest;
import com.BackendChallenge.TechTrendEmporium.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import com.BackendChallenge.TechTrendEmporium.entity.Category;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.repository.CategoryRepository;
import com.BackendChallenge.TechTrendEmporium.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)

class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;
    @Mock
    private Product existingProduct; // Mock Product object

    private Product[] fakeProducts;
    private Category dummyCategory;
    //private static final String EXTERNAL_API_URL_PRODUCTS = "https://fakestoreapi.com/products";

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, categoryRepository);

        // Setting up fake products
        fakeProducts = new Product[]{
                new Product(),
                new Product()
        };
        fakeProducts[0].setId(345L);
        fakeProducts[0].setTitle("John Hardy Women's Legends Naga Gold & Silver Dragon Station Chain Bracelet");
        fakeProducts[0].setPrice(695.0);
        fakeProducts[0].setCategory("jewelery");
        fakeProducts[0].setCategoryId(30L);
        fakeProducts[0].setDescription("From our Legends Collection, the Naga was inspired by the mythical water dragon that protects the ocean's pearl. Wear facing inward to be bestowed with love and abundance, or outward for protection.");
        fakeProducts[0].setImage("https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_.jpg");
        //fakeProducts[0].setRating(new Rating(4.6, 400));

        fakeProducts[1].setId(346L);
        fakeProducts[1].setTitle("Solid Gold Petite Micropave");
        fakeProducts[1].setPrice(168.0);
        fakeProducts[1].setCategory("jewelery");
        fakeProducts[1].setCategoryId(30L);
        fakeProducts[1].setDescription("Satisfaction Guaranteed. Return or exchange any order within 30 days.Designed and sold by Hafeez Center in the United States. Satisfaction Guaranteed. Return or exchange any order within 30 days.");
        fakeProducts[1].setImage("https://fakestoreapi.com/img/61sbMiUnoGL._AC_UL640_QL65_ML3_.jpg");

        dummyCategory = new Category();
        dummyCategory.setId(1L);
        dummyCategory.setName("men's clothing");
        //akeProducts[1].setRating(new Rating(3.9, 70));
    }

    @Test
    void testFetchAndSaveProducts() {
        // Mocking restTemplate to return fake products

        when(productRepository.count()).thenReturn(0L);

        when(categoryRepository.findByName("men's clothing")).thenReturn(dummyCategory);

        // Mocking product repository saveAll method
        when(productRepository.saveAll(any())).thenReturn(Arrays.asList(fakeProducts));

        // Calling the method under test
        productService.fetchAndSaveProducts();

        verify(productRepository, times(1)).count();
        verify(categoryRepository, times(4)).findByName("men's clothing");


        ArgumentCaptor<List<Product>> captor;
        captor = ArgumentCaptor.forClass(List.class);
        verify(productRepository).saveAll(captor.capture());
        List<Product> savedProducts = captor.getValue();
        assertEquals(20, savedProducts.size());
    }

    @Test
    void testGetPaginatedList() {
        // Create a list of products for testing
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setId((long) i);
            product.setTitle("Product " + i);
            productList.add(product);
        }

        // Define page and size for pagination
        int page = 1;
        int size = 5;

        // Call the method under test
        List<Product> result = productService.getPaginatedList(productList,page, size);

        // Assert the size of the result list
        assertEquals(size, result.size(), "Size of paginated list should be equal to the specified size");

        // Assert the contents of the result list
        for (int i = 0; i < size; i++) {
            assertEquals("Product " + (page * size + i), result.get(i).getTitle(),
                    "Incorrect product title in paginated list");
        }
    }
    @Test
    void testConvertToDTO() {
        // Create a sample Product object
        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setDescription("This is a test product");
        product.setPrice(100.0);

        // Create a Rating object and set its properties
        Product.Rating rating = new Product.Rating();
        rating.setRate(4.5);
        rating.setCount(100);
        product.setRating(rating);

        // Create an Inventory object and set its properties
        Product.Inventory inventory = new Product.Inventory();
        inventory.setTotal(10);
        inventory.setAvailable(5);
        product.setInventory(inventory);

        // Call the method under test
        ProductDTO productDTO = productService.convertToDTO(product);

        // Assert that the ProductDTO is not null
        assertNotNull(productDTO, "Converted ProductDTO should not be null");

        // Assert that the fields are correctly mapped
        assertEquals(product.getId(), productDTO.getId(), "Id should be mapped correctly");
        assertEquals(product.getTitle(), productDTO.getTitle(), "Title should be mapped correctly");
        assertEquals(product.getDescription(), productDTO.getDescription(), "Description should be mapped correctly");
        assertEquals(product.getPrice(), productDTO.getPrice(), "Price should be mapped correctly");

        // Assert that the nested objects are correctly mapped
        assertEquals(product.getRating().getRate(), productDTO.getRating().getRate(), "Rating rate should be mapped correctly");
        assertEquals(product.getRating().getCount(), productDTO.getRating().getCount(), "Rating count should be mapped correctly");
        assertEquals(product.getInventory().getTotal(), productDTO.getInventory().getTotal(), "Inventory total should be mapped correctly");
        assertEquals(product.getInventory().getAvailable(), productDTO.getInventory().getAvailable(), "Inventory available should be mapped correctly");
    }
//    @Test
//    void testGetAllProducts() {
//        // Mock data
//        List<Product> mockProducts = List.of(new Product(), new Product());
//
//        // Mock repository behavior
//        int page = 0;
//        int size = 10;
//        when(productRepository.findAll(PageRequest.of(page, size)))
//                .thenReturn(new PageImpl<>(mockProducts));
//
//        // Call the method under test
//        List<Product> result = productService.getAllProducts();
//
//        // Assert that the result is not null
//        assertNotNull(result, "Result should not be null");
//    }
    @Test
    void testGetProductById() {
        // Mock data
        Long productId = 345L;

        // Mock repository behavior
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(fakeProducts[0]));

        // Call the method under test
        Product result = productService.getProductById(productId);

        // Assert that the result matches the mock product
        assertEquals(fakeProducts[0], result, "Returned product should match mock product");
    }
    @Test
    void testGetProductsByCategory() {
        // Mock data
        String category = "jewelery";
        Product product1 = new Product();
        product1.setTitle("Product 1");
        Product product2 = new Product();
        product2.setTitle("Product 2");
        List<Product> expectedProducts = Arrays.asList(product1, product2);

        // Mock repository behavior
        when(productRepository.findByCategory(category)).thenReturn(expectedProducts);

        // Call the method under test
        List<Product> result = productService.getProductsByCategory(category);

        // Assert that the result matches the expected products
        assertEquals(expectedProducts, result, "Returned products should match expected products");
    }
    @Test
    void testGetAllProductsSortedByPrice_Ascending() {
        // Mock data
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(fakeProducts[1]);
        expectedProducts.add(fakeProducts[0]);

        // Mock repository behavior
        when(productRepository.findAll(Sort.by("price"))).thenReturn(expectedProducts);

        // Call the method under test
        List<Product> result = productService.getAllProductsSortedByPrice("asc");

        // Assert that the result matches the expected products
        assertEquals(expectedProducts, result, "Returned products should match expected products");
    }

    @Test
    void testGetAllProductsSortedByPrice_Descending() {
        // Mock data
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(fakeProducts[0]);
        expectedProducts.add(fakeProducts[1]);

        // Mock repository behavior
        when(productRepository.findAll(Sort.by("price").descending())).thenReturn(expectedProducts);

        // Call the method under test
        List<Product> result = productService.getAllProductsSortedByPrice("desc");

        // Assert that the result matches the expected products
        assertEquals(expectedProducts, result, "Returned products should match expected products");
    }
    @Test
    void testGetAllProductsSortedByTitle_Ascending() {
        // Mock data
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(fakeProducts[0]);
        expectedProducts.add(fakeProducts[1]);

        // Mock repository behavior
        when(productRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))).thenReturn(expectedProducts);

        // Call the method under test
        List<Product> result = productService.getAllProductsSortedByTitle("asc");

        // Assert that the result matches the expected products
        assertEquals(expectedProducts, result, "Returned products should match expected products");
    }
    @Test
    void testGetAllProductsSortedByTitle_Descending() {
        // Mock data
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(fakeProducts[1]);
        expectedProducts.add(fakeProducts[0]);

        // Mock repository behavior
        when(productRepository.findAll(Sort.by(Sort.Direction.DESC, "title"))).thenReturn(expectedProducts);

        // Call the method under test
        List<Product> result = productService.getAllProductsSortedByTitle("desc");

        // Assert that the result matches the expected products
        assertEquals(expectedProducts, result, "Returned products should match expected products");
    }
    @Test
    void testFilterApprovedProducts() {
        // Create a list of products with different statuses
        Product product1 = new Product();
        product1.setStatus(null);

        Product product2 = new Product();
        product2.setStatus(ProductStatus.APPROVED);

        Product product3 = new Product();
        product3.setStatus(ProductStatus.PENDING_CREATION);

        // Add the products to a list
        List<Product> products = Arrays.asList(product1, product2, product3);

        // Call the method under test
        List<Product> approvedProducts = productService.filterApprovedProducts(products);

        // Assert that only approved or null status products are returned
        assertEquals(2, approvedProducts.size(), "Expected number of approved products");
        assertEquals(product1, approvedProducts.get(0), "First product should be returned");
        assertEquals(product2, approvedProducts.get(1), "Second product should be returned");
    }
    @Test
    void testCreateProduct() {
        // Use the first product from the fakeProducts array as the sample product
        Product product = fakeProducts[0];
        Category fakeCategory = new Category();
        fakeCategory.setId(30L);
        fakeCategory.setName("jewelry");


        // Mock the behavior of the productRepository.save method
        when(productRepository.save(product)).thenReturn(product);
        // Mock the behavior of the categoryRepository.findByName method to return a Category object
        when(categoryRepository.findByName(anyString())).thenReturn(fakeCategory);



        // Call the method under test
        Product savedProduct = productService.createProduct(product);

        // Verify that the save method is called with the correct argument
        verify(productRepository).save(product);

        // Assert that the returned product is the same as the input product
        assertEquals(product, savedProduct, "Saved product should be the same as the input product");
    }
    @Test
    void testDeleteProduct() {
        // Sample product id to delete
        Long productId = 1L;

        // Call the method under test
        productService.deleteProduct(productId);

        // Verify that the deleteById method is called with the correct productId
        verify(productRepository).deleteById(productId);
    }
    @Test
    void testPendingDeleteProduct() {
        // Sample product id
        Long productId = 1L;

        // Mock product
        Product product = new Product();
        product.setId(productId);

        // Mock the behavior of productRepository.findById
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call the method under test
        productService.pendingDeleteProduct(productId);

        assertEquals(product.getStatus(),ProductStatus.PENDING_DELETE);

        // Verify that the product is saved
        verify(productRepository).save(product);
    }
    @Test
    public void testUpdateProduct() {
        // Mock data
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setId(1L);
        request.setTitle("Updated Title");
        request.setPrice(99.99);

        // Set a default Inventory object
        ProductUpdateRequest.Inventory inventory = new ProductUpdateRequest.Inventory();
        inventory.setTotal(20);
        inventory.setAvailable(10);
        request.setInventory(inventory);

        // Set a default Rating object
        ProductUpdateRequest.Rating rating = new ProductUpdateRequest.Rating();
        rating.setRate(4.5);
        rating.setCount(100);
        request.setRating(rating);

        // Create existing product
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        // Set other fields as needed

        // Mock the behavior of productRepository.findById
        when(productRepository.findById(request.getId())).thenReturn(Optional.of(existingProduct));

        // Call the method under test
        productService.updateProduct(request);

        // Verify that productRepository.save(existingProduct) is called
        verify(productRepository, times(1)).save(existingProduct);
    }

}
