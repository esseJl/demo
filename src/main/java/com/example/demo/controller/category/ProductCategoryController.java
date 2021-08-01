package com.example.demo.controller.category;

import com.example.demo.model.category.ProductCategory;
import com.example.demo.model.image.Image;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/category")
public class ProductCategoryController {
    private CategoryService categoryService;
    private final String active="category";

    @Autowired
    public ProductCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public String category(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("category", new ProductCategory());
        model.addAttribute("Active",active);
        return "category";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String saveNewCategory(@ModelAttribute ProductCategory category) {
        categoryService.saveCategory(category);
        return "redirect:/category/home";
    }


    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateCategory(@RequestParam(name = "categoryId", required = true) String categoryUUID,
                                 @RequestParam(name = "categoryName", required = true) String categoryName) {
//                ^ ^  TODO safe requirement ^|^


        ProductCategory category = categoryService.updateCategory(categoryUUID, categoryName);
        //TODO add @RedirectAttribute and support on frontEnd.
        return "redirect:/category/home";
    }

    @ResponseBody
    @RequestMapping(value = "/{categoryUUID}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductCategory getCategory(HttpServletResponse response,
                                       @PathVariable("categoryUUID") String categoryUUID) throws IOException {
        return categoryService.findOne(categoryUUID);
    }
}
