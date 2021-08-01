package com.example.demo.service.brand;

import com.example.demo.model.brand.ProductBrand;
import com.example.demo.model.image.Image;
import com.example.demo.model.repository.brand.ProductBrandRepository;
import com.example.demo.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrandService {

    private ProductBrandRepository brandRepository;
    private ImageService imageService;

    @Autowired
    public BrandService(ProductBrandRepository brandRepository, ImageService imageService) {
        this.brandRepository = brandRepository;
        this.imageService = imageService;
    }

    public ProductBrand saveNewBrand(ProductBrand brand, MultipartFile multipartFile) {

        Image image = imageService.saveNewImage(multipartFile);

        brand.setImage(image);
        brand.setBrandUUID(UUID.randomUUID().toString());
        image.setBrand(brand);
        //brand.setId(null);
        return brandRepository.save(brand);
    }

    public List<ProductBrand> getAllBrand() {
        return brandRepository.findAll();
    }


    public ProductBrand getBrand(String UUID) {
        Optional<ProductBrand> byBrandUUID = brandRepository.findByBrandUUID(UUID);
        byBrandUUID.orElseThrow();
        //System.out.println(byBrandUUID.getBrandName());
        return byBrandUUID.get();
    }

    public ProductBrand updateBrandName(String brandName, String brandUUID) {
        Optional<ProductBrand> byBrandUUID = brandRepository.findByBrandUUID(brandUUID);
        byBrandUUID.orElseThrow();
        // TODO byBrandUUID can not NULL ^_^ support Exception class

        byBrandUUID.get().setBrandName(brandName);
        return brandRepository.save(byBrandUUID.get());

    }
}
