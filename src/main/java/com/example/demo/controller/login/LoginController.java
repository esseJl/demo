package com.example.demo.controller.login;

import com.example.demo.model.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(path = "/login-success", method = RequestMethod.GET)
    public String loginSuccess() {
        return "login-success";
    }

    @RequestMapping(path = "/forgetPassword", method = RequestMethod.GET)
    public String forgotPassword() {
        return "forgotPasswordPage";
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
