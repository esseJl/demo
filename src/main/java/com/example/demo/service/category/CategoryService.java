package com.example.demo.service.category;

import com.example.demo.model.category.ProductCategory;
import com.example.demo.model.repository.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;


    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ProductCategory saveCategory(ProductCategory category) {
        category.setCategoryUUID(UUID.randomUUID().toString());
        return categoryRepository.save(category);
    }

    public List<ProductCategory> getAllCategory() {
        return categoryRepository.findAll();
    }

    public ProductCategory findOne(String categoryUUID) {
        Optional<ProductCategory> byCategoryUUID = categoryRepository.findByCategoryUUID(categoryUUID);
        byCategoryUUID.orElseThrow();
        return byCategoryUUID.get();
    }

    public ProductCategory updateCategory(String categoryUUID, String categoryName) {
        ProductCategory category = findOne(categoryUUID);
        category.setName(categoryName);
        return categoryRepository.save(category);
    }
}
