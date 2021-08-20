package com.example.demo.model.category.cat4post;

import com.example.demo.model.category.CategoryAbstract;
import com.example.demo.model.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class PostCategory extends CategoryAbstract {

    @ManyToMany(mappedBy = "postCategory"
            , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Post> posts;

    public PostCategory() {
        super();
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
