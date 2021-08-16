package com.example.demo.controller.account.managment.forgot.password;

import cn.apiclub.captcha.Captcha;
import com.example.demo.model.user.User;
import com.example.demo.model.user.password.Password;
import com.example.demo.model.user.token.reset.password.token.PasswordResetToken;
import com.example.demo.service.token.reset.password.PasswordResetService;
import com.example.demo.service.user.UserService;
import com.example.demo.stc.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ForgotPasswordController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder encoder;

    @Autowired
    public ForgotPasswordController(UserService userService,
                                    PasswordResetService passwordResetService,
                                    PasswordEncoder encoder) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
        this.encoder = encoder;
    }

    @RequestMapping(path = "/forgotPassword", method = RequestMethod.GET)
    public String forgotPassword(Model model, HttpServletRequest request) {
        User user = new User();
        getCaptcha(user);
        String key = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute(key, user.getHiddenCaptcha());
        user.setHiddenCaptcha(key);
        model.addAttribute("user", user);
        return "account/management/forgot-password/forgot-password-page";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@ModelAttribute User user, @RequestParam(name = "email", required = true, defaultValue = "") String email,
                                 Model model, HttpServletRequest request) {

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
            return "account/management/forgot-password/forgot-password-page";
        }
        getCaptcha(user);
        String key = UUID.randomUUID().toString();
        HttpSession newSession = request.getSession();
        newSession.setAttribute(key, user.getHiddenCaptcha());
        user.setHiddenCaptcha(key);

        if (email.trim().equals("")) {
            model.addAttribute("error", "email can not be empty \n Enter Current Email that you register with");
            return "account/management/forgot-password/forgot-password-page";
        }
        if (!userService.isUserPresentWithEmail(email)) {
            model.addAttribute("error", "email not found");
            return "account/management/forgot-password/forgot-password-page";
        }
        User userByEmail = userService.findUserByEmail(email);
        if (userService.doesUserHaveResetPasswordToken(userByEmail)) {
            model.addAttribute("error", "ResetPassword and not Expired yet. if you didnt receive any email try it 10 min later.");
            return "account/management/forgot-password/forgot-password-page";
        }
        String appUrl =
                "http://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();
        User userRestPassword = userService.sendResetPasswordEmail(userByEmail, appUrl);
        model.addAttribute("userSuccess", userRestPassword);
        newSession.removeAttribute(hideCaptcha);
        return "account/management/forgot-password/forgot-password-page";
    }

    @GetMapping("/resetPasswordConfirm/{token}")
    public String confirmResetPassword
            (Model model, HttpServletRequest request, @PathVariable("token") String token) {

        Optional<PasswordResetToken> byToken = passwordResetService.findByToken(token);
        if (byToken.isEmpty()) {
            //String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("errorAuthMessageExpired", "auth.message.invalidToken");
            //return "redirect:/badUser.html?lang=" + locale.getLanguage();
            return "account/management/forgot-password/reset-password-confirm-page";
        }

        if ((byToken.get().getExpiryDate().before(new Date()))) {

            //String messageValue = messages.getMessage("auth.message.expired", null, locale)
            model.addAttribute("errorAuthMessageExpired", "auth.message.expired");
            model.addAttribute("expireDate", byToken.get().getExpiryDate());
            passwordResetService.deleteById(byToken.get().getId());
            //return "redirect:/badUser.html?lang=" + locale.getLanguage();
            return "account/management/forgot-password/reset-password-confirm-page";
        }
        model.addAttribute("passwordResetToken", byToken.get().getToken());
        model.addAttribute("Password", new Password());
        User user = new User();
        getCaptcha(user);
        String key = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute(key, user.getHiddenCaptcha());
        user.setHiddenCaptcha(key);
        model.addAttribute("user", user);
        return "account/management/forgot-password/reset-password-confirm-page";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid @ModelAttribute(name = "Password") Password password,
                                 BindingResult bindingResult,
                                 @RequestParam(name = "resetPasswordToken", required = true, defaultValue = "") String token,
                                 Model model, @ModelAttribute User user, HttpServletRequest request) {

        final String path = "/resetPasswordConfirm/" + token;
        if (token.trim().equals("")) {
            return "redirect:" + path;
        }
        Optional<PasswordResetToken> byToken = passwordResetService.findByToken(token);
        if (byToken.isEmpty()) {
            return "redirect:" + path;
        }
        if ((byToken.get().getExpiryDate().before(new Date()))) {
            return "redirect:" + path;
        }

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
            return "account/management/forgot-password/reset-password-confirm-page";
        }
        getCaptcha(user);
        String key = UUID.randomUUID().toString();
        HttpSession newSession = request.getSession();
        newSession.setAttribute(key, user.getHiddenCaptcha());
        user.setHiddenCaptcha(key);

        model.addAttribute("passwordResetToken", token);
        if (bindingResult.hasErrors()) {
            return "account/management/forgot-password/reset-password-confirm-page";
        }
        if (!password.getPassword().equals(password.getMatchingPassword())) {
            //model.addAttribute("errorNotMatchPassword", "NotMatchPassword");
            bindingResult.rejectValue("matchingPassword", "matchingPassword.password", "Not.match.pass.conf.pass");
            return "account/management/forgot-password/reset-password-confirm-page";
        }

        newSession.removeAttribute(hideCaptcha);

        User userResetPassword = byToken.get().getUser();
        userResetPassword.setPassword(encoder.encode(password.getPassword()));
        userService.saveUser(userResetPassword);
        passwordResetService.deleteById(byToken.get().getId());
        final String message = "?message=password.updated";
        return "redirect:/login" + message;
    }

    private void getCaptcha(User user) {
        Captcha captcha = CaptchaUtil.createCaptcha(220, 70);
        user.setHiddenCaptcha(captcha.getAnswer());
        user.setCaptcha(""); // value entered by the User
        user.setRealCaptcha(CaptchaUtil.encodeCaptcha(captcha));
    }
}
