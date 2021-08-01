package com.example.demo.model.repository.image;

import com.example.demo.model.image.Image;
import com.example.demo.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> getImageByImageUUID(String imageUUID);

    List<Image> findAllByProduct(Product product);

    void deleteById(Long id);
}
