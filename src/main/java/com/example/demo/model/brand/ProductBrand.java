package com.example.demo.model.brand;


import com.example.demo.model.image.Image;
import com.example.demo.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProductBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private String brandUUID;

    private String brandName;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JsonIgnore
    private Image image;

    @OneToMany(mappedBy = "brand"
            , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;

    public ProductBrand() {
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public String getBrandUUID() {
        return brandUUID;
    }

    public void setBrandUUID(String brandUUID) {
        this.brandUUID = brandUUID;
    }
}
