package com.example.demo.model.category.cat4product;

import com.example.demo.model.category.CategoryAbstract;
import com.example.demo.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class ProductCategory extends CategoryAbstract implements Serializable {

    @OneToMany(mappedBy = "productCategory"
            , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;


    public ProductCategory() {
        super();
    }


    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }


}
