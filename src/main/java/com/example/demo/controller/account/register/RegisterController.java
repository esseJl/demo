package com.example.demo.controller.account.register;

import cn.apiclub.captcha.Captcha;
import com.example.demo.model.user.User;
import com.example.demo.service.user.UserService;
import com.example.demo.stc.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.UUID;

@Controller
public class RegisterController {

    private final UserService userService;


    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/register")
    public String registerForm(Model model, HttpServletRequest request) {
        User user = new User();
        getCaptcha(user);
        String key = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute(key, user.getHiddenCaptcha());
        user.setHiddenCaptcha(key);
        model.addAttribute("user", user);
        return "account/register/register";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute User user,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request) {

        HttpSession session = request.getSession();
        String hideCaptcha = (String) session.getAttribute(user.getHiddenCaptcha());
        session.removeAttribute(hideCaptcha);
        if (!user.getCaptcha().equals(hideCaptcha)) {
            //System.out.println(user.getCaptcha());
            //System.out.println(user.getHiddenCaptcha());
            getCaptcha(user);
            String key = UUID.randomUUID().toString();
            HttpSession newSession = request.getSession();
            newSession.setAttribute(key, user.getHiddenCaptcha());
            user.setHiddenCaptcha(key);
            model.addAttribute("user", user);
            model.addAttribute("captchaMessage", "invalid.captcha");
            return "account/register/register";
        }
        getCaptcha(user);
        String key = UUID.randomUUID().toString();
        HttpSession newSession = request.getSession();
        newSession.setAttribute(key, user.getHiddenCaptcha());
        user.setHiddenCaptcha(key);
        model.addAttribute("user", user);
        if (bindingResult.hasErrors()) {
            return "account/register/register";
        }
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            //model.addAttribute("errorNotMatchPassword", "NotMatchPassword");
            bindingResult.rejectValue("matchingPassword", "matchingPassword.user", "Not.match.pass.conf.pass");
            return "account/register/register";
        }
        if (userService.isUserPresentWithUserName(user.getUserName())) {
            //bindingResult.rejectValue("userName", "userName.user", "username.already.exist");
            model.addAttribute("errors", "errors.user.with.username.exist");
            return "account/register/register";
        }
        if (userService.isUserPresentWithEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "email.user", "email.already.exist");
            return "account/register/register";
        }
        User registered = userService.registerNewUserAccount(user, request);
        redirectAttributes.addFlashAttribute("saveUserSuccess", registered);
        newSession.removeAttribute(hideCaptcha);
        return "redirect:/login-success";


    }

    @RequestMapping(path = "/login-success", method = RequestMethod.GET)
    public String loginSuccess() {
        return "account/register/register-success";
    }


    private void getCaptcha(User user) {
        Captcha captcha = CaptchaUtil.createCaptcha(220, 70);
        user.setHiddenCaptcha(captcha.getAnswer());
        user.setCaptcha(""); // value entered by the User
        user.setRealCaptcha(CaptchaUtil.encodeCaptcha(captcha));
    }
}
