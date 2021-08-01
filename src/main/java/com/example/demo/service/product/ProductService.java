package com.example.demo.service.product;


import com.example.demo.model.brand.ProductBrand;
import com.example.demo.model.image.Image;
import com.example.demo.model.product.Product;
import com.example.demo.model.repository.product.ProductRepository;
import com.example.demo.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ImageService imageService;

    @Autowired
    public ProductService(ProductRepository productRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.imageService = imageService;
    }

    public Product saveProduct(Product product) {
        product.setProductUUID(UUID.randomUUID().toString());
        return productRepository.save(product);
    }

    public List<Product> getAllProduct() {
        List<Product> all = productRepository.findAll();
        return all;
    }

    public Product getProduct(String productUUID) {
        Optional<Product> byProductUUID = productRepository.findByProductUUID(productUUID);
        byProductUUID.orElseThrow();

        return byProductUUID.get();
    }

    public boolean saveNewImage4product(String productId, MultipartFile file) {
        Product product = getProduct(productId);
        Image image = imageService.saveNewImage(file);
        image.setProduct(product);
        //image.setBrand(null);
        imageService.saveImage(image);
        return true;
    }
}
