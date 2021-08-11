package com.example.demo.model.image;

import com.example.demo.model.brand.ProductBrand;
import com.example.demo.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private String imageUUID;

    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @OneToOne(mappedBy = "image", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JsonIgnore
    private ProductBrand brand;


    public Image() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUUID() {
        return imageUUID;
    }

    public void setImageUUID(String imageId) {
        this.imageUUID = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageUrl) {
        this.imageName = imageUrl;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductBrand getBrand() {
        return brand;
    }

    public void setBrand(ProductBrand brand) {
        this.brand = brand;
    }

}
