package com.example.demo.controller.register;

import com.example.demo.model.user.User;
import com.example.demo.model.user.email.verification.token.VerificationToken;
import com.example.demo.repository.verification.token.VerificationTokenRepository;
import com.example.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class RegisterController {

    private UserService userService;
    private VerificationTokenRepository verificationTokenRepository;


    @Autowired
    public RegisterController(UserService userService, VerificationTokenRepository verificationTokenRepository) {
        this.userService = userService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute User user,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            //model.addAttribute("errorNotMatchPassword", "NotMatchPassword");
            bindingResult.rejectValue("matchingPassword", "matchingPassword.user", "Not.match.pass.conf.pass");
            return "register";
        }
        if (userService.isUserPresentWithUserName(user.getUserName())) {
            //bindingResult.rejectValue("userName", "userName.user", "username.already.exist");
            model.addAttribute("errors", "errors.user.with.username.exist");
            return "register";
        }
        if (userService.isUserPresentWithEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "email.user", "email.already.exist");
            return "register";
        }
        User registered = userService.registerNewUserAccount(user, request);
        redirectAttributes.addFlashAttribute("saveUserSuccess", registered);
        return "redirect:/login-success";
    }

    @GetMapping("/verifyEmail")
    public String getVerificationTokenForEmail() {
        return "verificationTokenEmailPage";
    }

    @PostMapping("/verifyEmail")
    public String verifyEmail(@RequestParam(name = "email", required = true, defaultValue = "") String email,
                              Model model, HttpServletRequest request) {
        if (email.trim().equals("")) {
            model.addAttribute("error", "email can not be empty \n Enter Current Email that you register with");
            return "verificationTokenEmailPage";
        }
        if (!userService.isUserPresentWithEmail(email)) {
            model.addAttribute("error", "email not found");
            return "verificationTokenEmailPage";
        }
        User userByEmail = userService.findUserByEmail(email);
        if (userService.doesUserHaveToken(userByEmail)) {
            model.addAttribute("error", "verifyEmailSendToYourEmail and not Expired yet. if you didnt receive any email try it 10 min later.");
            return "verificationTokenEmailPage";
        }
        //userService.createVerificationToken(userByEmail, UUID.randomUUID().toString());
        User user = userService.sendVerifyEmail(userByEmail, request);
        model.addAttribute("userSuccess", user);
        return "verificationTokenEmailPage";
    }

    @GetMapping("/verificationRegisterConfirm")
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token) {

        //Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            //String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", "auth.message.invalidToken");
            //return "redirect:/badUser.html?lang=" + locale.getLanguage();
            return "verificationRegisterConfirmPage";
        }

        User user = verificationToken.getUser();
        //Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().before(new Date()))) {

            //String messageValue = messages.getMessage("auth.message.expired", null, locale)
            model.addAttribute("message", "auth.message.expired");
            model.addAttribute("expireDate", verificationToken.getExpiryDate());
            verificationTokenRepository.deleteById(verificationToken.getId());
            //return "redirect:/badUser.html?lang=" + locale.getLanguage();
            return "verificationRegisterConfirmPage";
        }

        user.setEnable(true);
        userService.saveRegisteredUser(user);
        verificationTokenRepository.deleteById(verificationToken.getId());
        return "redirect:/login?saveUserSuccess=true";
    }
}
