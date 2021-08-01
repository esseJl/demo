package com.example.demo.model.repository.product.category;

import com.example.demo.model.category.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByCategoryUUID(String categoryUUID);
}
