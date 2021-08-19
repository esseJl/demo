package com.example.demo.controller.post;

import com.example.demo.model.post.Post;
import com.example.demo.model.user.principal.UserPrincipal;
import com.example.demo.service.post.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @RequestMapping(method = RequestMethod.GET, path = "/admin/post")
    public String newPostForm(Model model, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        model.addAttribute(userPrincipal);
        model.addAttribute("post", new Post());
        return "admin/blog/blog-new-post-form";
    }

}
