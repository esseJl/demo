package com.example.demo.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class HomeController {


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String adminHome() {
        return "admin/layout";
    }

    @RequestMapping(value = "/blog", method = RequestMethod.GET)
    public String blogHome() {
        return "admin/blog-list";
    }

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public String newPost() {
        return "admin/post-page";
    }


}
