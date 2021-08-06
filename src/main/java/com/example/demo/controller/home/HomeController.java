package com.example.demo.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "redirect:/login" + "?logout";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }
}
