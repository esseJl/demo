package com.example.demo.controller.blog.post;

import com.example.demo.model.category.cat4post.Category4Post;
import com.example.demo.model.post.Post;
import com.example.demo.model.post.state.PostState;
import com.example.demo.model.user.principal.UserPrincipal;
import com.example.demo.service.category.cat4post.Category4PostService;
import com.example.demo.service.post.PostService;
import com.example.demo.service.tag.TagService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class PostController {

    private PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;

    }


    @RequestMapping(path = "/posts", method = RequestMethod.GET)
    public String postLists(Model model) {
        return "/admin/blog/blog-list";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/post")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("states", PostState.values());
        return "/admin/blog/blog-details";
    }


    @RequestMapping(method = RequestMethod.POST, path = "/post")
    public String saveNewPost(@Valid @ModelAttribute Post post, BindingResult bindingResult,
                              @AuthenticationPrincipal UserPrincipal userPrincipal,
                              @RequestParam(value = "category", required = false) List<String> categories,
                              @RequestParam(value = "tag", required = false) List<String> tags,
                              @RequestParam(name = "comment-enable", required = false, defaultValue = "") String commentEnable,
                              @RequestParam(name = "state", required = false, defaultValue = "") String state,
                              Model model) {

        model.addAttribute("states", PostState.values());


        if (postService.isUniqueLinkExist(post.getUniqueLink())) {
            model.addAttribute("uniqueLinkError", "already.exist.unique.link");
            return "/admin/blog/blog-details";
        }

        if (bindingResult.hasErrors()) {
            return "/admin/blog/blog-details";
        }

        Post newPost = postService.createNewPost(post, userPrincipal.getUser());
        final String path = "/admin/post/" + newPost.getPostUUID();
        return "redirect:" + path;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/post/{postUUID}")
    public String postManager(@PathVariable("postUUID") String postUUID,
                              Model model) {
        Post postByPostUUID = postService.findPostByPostUUID(postUUID);
        if (postByPostUUID == null) {
            final String path = "/admin/post";
            return "redirect:" + path;
        }
        model.addAttribute("categoryField", new Category4Post());
        model.addAttribute("postCategory", postByPostUUID.getPostCategory());
        model.addAttribute("post", postByPostUUID);
        return "/admin/blog/blog-details";
    }


}
