package com.BackendChallenge.TechTrendEmporium.service;

import com.BackendChallenge.TechTrendEmporium.dto.ProductDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Category;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.ProductStatus;
import com.BackendChallenge.TechTrendEmporium.entity.ProductUpdateRequest;
import com.BackendChallenge.TechTrendEmporium.repository.CategoryRepository;
import com.BackendChallenge.TechTrendEmporium.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.NoSuchElementException;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final String EXTERNAL_API_URL_PRODUCTS = "https://fakestoreapi.com/products";

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public void fetchAndSaveProducts() {
        // Fetch products from external API
        RestTemplate restTemplate = new RestTemplate();
        Product[] products = restTemplate.getForObject(EXTERNAL_API_URL_PRODUCTS, Product[].class);

        // Set category ID for each product before saving to the database
        assert products != null;
        for (Product product : products) {
            String categoryName = product.getCategory();
            // Find category by name
            Category category = categoryRepository.findByName(categoryName);
            if (category != null) {
                // Set categoryId in the product entity
                product.setCategoryId((category.getId()));
            } else {
                //  where category is not found , **could throw an exception if wanted
                product.setCategoryId(0L);
            }
        }

        // Save products to the database
        if (products.length > 0 && productRepository.count() == 0) {
            productRepository.saveAll(Arrays.asList(products));
        }
    }
    public List<Product> getPaginatedList(List<Product> productList, int page, int size) {
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, productList.size());

        if (startIndex >= productList.size()) {
            return Collections.emptyList(); // Return empty list if startIndex exceeds list size
        }

        return productList.subList(startIndex, endIndex);
    }
    public ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        productDTO.setRating(new ProductDTO.RatingDTO(product.getRating().getRate(), product.getRating().getCount()));
        productDTO.setInventory(new ProductDTO.InventoryDTO(product.getInventory().getTotal(), product.getInventory().getAvailable()));
        return productDTO;
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }


    public List<Product> getAllProductsSortedByPrice(String sortOrder) {
        Sort sort = Sort.by("price");
        if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }
        return productRepository.findAll(sort);
    }


    public List<Product> getAllProductsSortedByTitle(String sortBy) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortBy != null && sortBy.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, "title"); // Sort by title field
        return productRepository.findAll(sort);
    }


    public List<Product> filterApprovedProducts(List<Product> products) {
        return products.stream()
                .filter(product -> product.getStatus() == null || product.getStatus() == ProductStatus.APPROVED)
                .collect(Collectors.toList());
    }


    public Product createProduct(Product product) {
        if (product.getCategory() == null) {
            // Set category to "Without_category" if not provided
            product.setCategory("Without_category");
        } else {
            // Validate if the provided category exists in the database
            Category existingCategory = categoryRepository.findByName(product.getCategory());
            if (existingCategory == null) {
                // Category does not exist, handle accordingly (throw exception, set default category, etc.)
                throw new IllegalArgumentException("Invalid category: " + product.getCategory());
            } else {
                // Set the product's category to match the category entity's name
                product.setCategory(existingCategory.getName());
            }
        }
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public void pendingDeleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setStatus(ProductStatus.PENDING_DELETE);
            productRepository.save(product);
        }
    }
    public void updateProduct(ProductUpdateRequest request) {
        Product existingProduct = productRepository.findById(request.getId())
                .orElseThrow(NoSuchElementException::new);
        existingProduct.setTitle(request.getTitle());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setImage(request.getImage());

        // Convert ProductUpdateRequest.Rating to Product.Rating
        Product.Rating productRating = new Product.Rating();
        productRating.setRate(request.getRating().getRate());
        productRating.setCount(request.getRating().getCount());
        existingProduct.setRating(productRating);

        // Convert ProductUpdateRequest.Inventory to Product.Inventory
        Product.Inventory productInventory = new Product.Inventory();
        productInventory.setTotal(request.getInventory().getTotal());
        productInventory.setAvailable(request.getInventory().getAvailable());
        existingProduct.setInventory(productInventory);

        productRepository.save(existingProduct);
    }
}

