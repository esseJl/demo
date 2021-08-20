package com.example.demo.model.product;


import com.example.demo.model.brand.ProductBrand;
import com.example.demo.model.category.cat4product.ProductCategory;
import com.example.demo.model.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private String productUUID;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String feature;

    @Column(length = 1024)
    private String description;

    private int price;
    private String size;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productBrand_id")
    @JsonIgnore
    private ProductBrand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productCategory_id")
    @JsonIgnore
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Image> images;

    public Product() {
    }

    public Product(ProductBrand brand, ProductCategory category) {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Image> getImages() {
        return images;
    }

    public void addImage(Image image) {
        this.images.add(image);
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public ProductBrand getBrand() {
        return brand;
    }

    public void setBrand(ProductBrand brand) {
        this.brand = brand;
    }

    public String getProductUUID() {
        return productUUID;
    }

    public void setProductUUID(String productId) {
        this.productUUID = productId;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
