package com.example.demo.service.category.cat4product;

import com.example.demo.model.category.cat4product.ProductCategory;
import com.example.demo.repository.category.cat4product.Cat4ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class Cat4ProductService {

    private Cat4ProductRepository cat4ProductRepository;


    @Autowired
    public Cat4ProductService(Cat4ProductRepository cat4ProductRepository) {
        this.cat4ProductRepository = cat4ProductRepository;
    }

    public ProductCategory saveCategory(ProductCategory category) {
        category.setCategoryUUID(UUID.randomUUID().toString());
        return cat4ProductRepository.save(category);
    }

    public List<ProductCategory> getAllCategory() {
        return cat4ProductRepository.findAll();
    }

    public ProductCategory findOne(String categoryUUID) {
        Optional<ProductCategory> byCategoryUUID = cat4ProductRepository.findByCategoryUUID(categoryUUID);
        byCategoryUUID.orElseThrow();
        return byCategoryUUID.get();
    }

    public ProductCategory updateCategory(String categoryUUID, String categoryName) {
        ProductCategory category = findOne(categoryUUID);
        category.setName(categoryName);
        return cat4ProductRepository.save(category);
    }
}
