package com.example.demo.controller.brand;

import com.example.demo.model.brand.ProductBrand;
import com.example.demo.service.brand.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/brand/admin")
public class BrandController {
    private BrandService brandService;
    private final String active = "brand";

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String brandHome(Model model) {
        model.addAttribute("brands", brandService.getAllBrand());
        model.addAttribute("brand", new ProductBrand());
        model.addAttribute("Active", active);
        return "brand";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String saveNewBrand(@RequestParam("file") MultipartFile file, @Valid @ModelAttribute ProductBrand brand,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "brand";
        }
        brandService.saveNewBrand(brand, file);
        return "redirect:/brand/home";
    }

    @RequestMapping(path = "/get/{brandUUID}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ProductBrand getBrand(@PathVariable("brandUUID") String brandUUID) {
        return brandService.getBrand(brandUUID);
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateBrandName(@RequestParam("brandName") String brandName, @RequestParam("branId") String UUID) {
        brandService.updateBrandName(brandName, UUID);
        return "redirect:/brand/home";
    }
}
