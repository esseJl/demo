package com.example.demo.repository.category.cat4post;

import com.example.demo.model.category.cat4post.Category4Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Category4PostRepository extends JpaRepository<Category4Post, Long> {

    Category4Post findByName(String name);

    Category4Post findByCategoryUUID(String postUUID);

    List<Category4Post> findByNameContains(String name);
}
