package com.example.demo.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class HomeController {


    @RequestMapping(value = "/layout", method = RequestMethod.GET)
    public String adminHome() {
        return "admin/layout";
    }


}
