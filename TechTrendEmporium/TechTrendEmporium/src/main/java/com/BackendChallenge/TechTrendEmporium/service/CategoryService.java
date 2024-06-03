package com.BackendChallenge.TechTrendEmporium.service;
import com.BackendChallenge.TechTrendEmporium.dto.CategoryDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Category;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryStatus;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryUpdateRequest;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    private static final String EXTERNAL_API_URL_CATEGORIES = "https://fakestoreapi.com/products/categories";

    public void fetchCategoryNames() {
        if (categoryRepository.count() == 0) {
            RestTemplate restTemplate = new RestTemplate();
            String[] categoryArray = restTemplate.getForObject(EXTERNAL_API_URL_CATEGORIES, String[].class);
            assert categoryArray != null;

            List<Category> categories = new ArrayList<>();
            for (String categoryName : categoryArray) {
                Category category = new Category();
                category.setName(categoryName);
                categories.add(category);
            }

            categoryRepository.saveAll(categories);
        }
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<CategoryDTO> getApprovedCategories() {
        List<CategoryStatus> approvedStatuses = Arrays.asList(CategoryStatus.APPROVED, null);
        List<Category> approvedCategories = categoryRepository.findByStatusInOrStatusIsNull(approvedStatuses);
        return approvedCategories.stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public void requestCategoryDeletion(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(NoSuchElementException::new);
        category.setStatus(CategoryStatus.PENDING_DELETE);
        categoryRepository.save(category); // Manually flush changes to the database
    }


    public void updateCategory(CategoryUpdateRequest request) {
        Category existingCategory = categoryRepository.findById(request.getId())
                .orElseThrow(NoSuchElementException::new);
        existingCategory.setName(request.getName());
        categoryRepository.save(existingCategory);
    }




}


