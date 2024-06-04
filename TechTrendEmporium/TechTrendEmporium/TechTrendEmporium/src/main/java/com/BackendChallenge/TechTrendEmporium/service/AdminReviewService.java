package com.BackendChallenge.TechTrendEmporium.service;

import com.BackendChallenge.TechTrendEmporium.dto.JobDTO;
import com.BackendChallenge.TechTrendEmporium.dto.ReviewJobRequestDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Category;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryStatus;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.ProductStatus;
import com.BackendChallenge.TechTrendEmporium.repository.CategoryRepository;
import com.BackendChallenge.TechTrendEmporium.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdminReviewService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<JobDTO> getPendingApprovalJobs() {
        List<JobDTO> jobs = new ArrayList<>();

        // Get products pending approval
        List<Product> pendingProducts = productRepository.findByStatus(ProductStatus.PENDING_CREATION);
        for (Product product : pendingProducts) {
            jobs.add(new JobDTO("product", product.getId(), "create"));
        }

        // Get categories pending approval
        List<Category> pendingCategories = categoryRepository.findByStatus(CategoryStatus.PENDING);
        for (Category category : pendingCategories) {
            jobs.add(new JobDTO("category", category.getId(), "create"));
        }

        // Get products pending deletion
        List<Product> pendingProductDeletions = productRepository.findByStatus(ProductStatus.PENDING_DELETE);
        for (Product product : pendingProductDeletions) {
            jobs.add(new JobDTO("product", product.getId(), "delete"));
        }

        // Get categories pending deletion
        List<Category> pendingCategoryDeletions = categoryRepository.findByStatus(CategoryStatus.PENDING_DELETE);
        for (Category category : pendingCategoryDeletions) {
            jobs.add(new JobDTO("category", category.getId(), "delete"));
        }

        return jobs;
    }


    public void reviewJob(String type, ReviewJobRequestDTO requestDTO) {
        if (type.equalsIgnoreCase("product")) {
            if (requestDTO.getOperation().equalsIgnoreCase("create")) {
                if (requestDTO.getAction().equalsIgnoreCase("approve")) {
                    approveProductCreation(requestDTO.getId());
                } else if (requestDTO.getAction().equalsIgnoreCase("decline")) {
                    denyProductCreation(requestDTO.getId());
                }
            } else if (requestDTO.getOperation().equalsIgnoreCase("delete")) {
                if (requestDTO.getAction().equalsIgnoreCase("approve")) {
                    approveProductDeletion(requestDTO.getId());
                } else if (requestDTO.getAction().equalsIgnoreCase("decline")) {
                    denyProductDeletion(requestDTO.getId());
                }
            }
        } else if (type.equalsIgnoreCase("category")) {
            if (requestDTO.getOperation().equalsIgnoreCase("create")) {
                if (requestDTO.getAction().equalsIgnoreCase("approve")) {
                    approveCategory(requestDTO.getId());
                } else if (requestDTO.getAction().equalsIgnoreCase("decline")) {
                    denyCategory(requestDTO.getId());
                }
            } else if (requestDTO.getOperation().equalsIgnoreCase("delete")) {
                if (requestDTO.getAction().equalsIgnoreCase("approve")) {
                    approveCategoryDeletion(requestDTO.getId());
                } else if (requestDTO.getAction().equalsIgnoreCase("decline")) {
                    denyCategoryDeletion(requestDTO.getId());
                }
            }
        }
    }

        // Approval and denial methods...
    public void approveProductCreation(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setStatus(ProductStatus.APPROVED);
            productRepository.save(product);
        }
    }

    public void denyProductCreation(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setStatus(ProductStatus.DENIED);
            productRepository.save(product);
        }
    }

    public void approveProductDeletion(Long productId) {
        productRepository.deleteById(productId);
    }

    public void denyProductDeletion(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setStatus(ProductStatus.APPROVED);
            productRepository.save(product);
        }
    }

    public void approveCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(NoSuchElementException::new);
        category.setStatus(CategoryStatus.APPROVED);
        categoryRepository.save(category);
    }

    public void denyCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(NoSuchElementException::new);
        category.setStatus(CategoryStatus.DENIED);
        categoryRepository.delete(category);
    }

    public void approveCategoryDeletion(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(NoSuchElementException::new);
        categoryRepository.delete(category);
    }


    public void denyCategoryDeletion(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(NoSuchElementException::new);
        category.setStatus(CategoryStatus.APPROVED);
        categoryRepository.save(category);
    }

}



