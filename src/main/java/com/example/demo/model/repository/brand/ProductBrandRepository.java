package com.example.demo.model.repository.brand;

import com.example.demo.model.brand.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long> {

    Optional<ProductBrand> findByBrandUUID(String UUID);
}
