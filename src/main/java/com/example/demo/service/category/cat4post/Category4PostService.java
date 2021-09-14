package com.example.demo.service.category.cat4post;


import com.example.demo.model.category.cat4post.Category4Post;
import com.example.demo.repository.category.cat4post.Category4PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class Category4PostService {

    private Category4PostRepository category4PostRepository;

    @Autowired
    public Category4PostService(Category4PostRepository category4PostRepository) {
        this.category4PostRepository = category4PostRepository;
    }

    public List<Category4Post> parseCategory(List<String> categoryNames) {

        Set<Category4Post> categories = new LinkedHashSet<>();
        if (categoryNames != null) {
            for (String catName : categoryNames) {
                if (catName != null && !catName.equals("")) {
                    Category4Post byName = category4PostRepository.findByName(catName);
                    if (byName == null)
                        byName = new Category4Post(catName, UUID.randomUUID().toString());
                    categories.add(byName);
                }
            }
        }
        return new ArrayList<>(categories);
    }

    public List<String> findByNameContains(String name) {
        List<String> catList = new ArrayList<>();
        List<Category4Post> byNameLike = category4PostRepository.findByNameContains(name);

        for (Category4Post cat : byNameLike) {
            catList.add(cat.getName());
        }
        return catList;
    }


}
