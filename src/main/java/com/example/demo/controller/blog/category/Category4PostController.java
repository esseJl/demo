package com.example.demo.controller.blog.category;

import com.example.demo.service.category.cat4post.Category4PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/post/category")
public class Category4PostController {

    private Category4PostService category4PostService;

    @Autowired
    public Category4PostController(Category4PostService category4PostService) {
        this.category4PostService = category4PostService;
    }

    @ResponseBody
    @RequestMapping(value = "/{nameLike}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> findByNameLike(@PathVariable String nameLike) {
        return category4PostService.findByNameContains(nameLike);
    }

}
