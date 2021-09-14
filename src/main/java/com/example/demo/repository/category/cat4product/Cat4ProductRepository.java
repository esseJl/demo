package com.example.demo.repository.category.cat4product;

import com.example.demo.model.category.cat4product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Cat4ProductRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByCategoryUUID(String categoryUUID);
}
