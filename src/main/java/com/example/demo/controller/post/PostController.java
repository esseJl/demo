package com.example.demo.controller.post;

import com.example.demo.service.post.PostService;
import org.springframework.stereotype.Controller;

@Controller
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    

}
