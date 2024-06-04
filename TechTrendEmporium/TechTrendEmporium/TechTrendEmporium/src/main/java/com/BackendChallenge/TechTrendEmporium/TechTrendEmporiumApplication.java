package com.BackendChallenge.TechTrendEmporium;

import com.BackendChallenge.TechTrendEmporium.service.CategoryService;
import com.BackendChallenge.TechTrendEmporium.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TechTrendEmporiumApplication {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	}

	public static void main(String[] args) {
		SpringApplication.run(TechTrendEmporiumApplication.class, args);
	}
	@PostConstruct
	public void init() {
		categoryService.fetchCategoryNames();
		productService.fetchAndSaveProducts();
	}

}
