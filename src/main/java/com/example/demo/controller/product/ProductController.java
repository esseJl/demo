package com.example.demo.controller.product;

import com.example.demo.model.brand.ProductBrand;
import com.example.demo.model.category.ProductCategory;
import com.example.demo.model.product.Product;
import com.example.demo.service.category.CategoryService;
import com.example.demo.service.brand.BrandService;
import com.example.demo.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;
    private CategoryService categoryService;
    private BrandService brandService;
    private final String active = "product";

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("brands", brandService.getAllBrand());
        List<Product> allProduct = productService.getAllProduct();
        model.addAttribute("products", allProduct);
        model.addAttribute("Active", active);
        return "product";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String saveNewProduct(@ModelAttribute Product product,
                                 @RequestParam("categoryUUID") String categoryUUID,
                                 @RequestParam("brandUUID") String brandUUID) {
        ProductCategory category = categoryService.findOne(categoryUUID);
        ProductBrand brand = brandService.getBrand(brandUUID);
        product.setBrand(brand);
        product.setProductCategory(category);
        productService.saveProduct(product);
        return "redirect:/product/home";
    }

    @RequestMapping(path = "/get/{productUUID}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    Product getProduct(@PathVariable("productUUID") String productUUID) {
        return productService.getProduct(productUUID);
    }

    @RequestMapping("/save/new/image")
    public String saveImage4product(@RequestParam("productId") String productUUID, @RequestParam("file") MultipartFile multipartFile) {
        boolean b = productService.saveNewImage4product(productUUID, multipartFile);
        // TODO provide redirectAttribute
        return "redirect:/product/home";
    }


}
