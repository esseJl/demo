package com.example.demo.controller.account.managment.email.verification;

import cn.apiclub.captcha.Captcha;
import com.example.demo.model.user.User;
import com.example.demo.model.user.token.email.verification.token.EmailVerificationToken;
import com.example.demo.repository.token.email.verification.EmailVerificationRepository;
import com.example.demo.service.user.UserService;
import com.example.demo.stc.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

@Controller
public class EmailVerificationController {

    private final UserService userService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    public EmailVerificationController(UserService userService,
                                       EmailVerificationRepository emailVerificationRepository) {
        this.userService = userService;
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @GetMapping("/verifyEmail")
    public String getVerificationTokenForEmail(Model model, HttpServletRequest request) {
        User user = new User();
        getCaptcha(user);
        String key = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute(key, user.getHiddenCaptcha());
        user.setHiddenCaptcha(key);
        model.addAttribute("user", user);
        return "account/management/verification-email/verification-token-email-page";
    }

    @PostMapping("/verifyEmail")
    public String verifyEmail(@ModelAttribute User user, @RequestParam(name = "email", required = true, defaultValue = "") String email,
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
            return "account/management/verification-email/verification-token-email-page";
        }
        getCaptcha(user);
        String key = UUID.randomUUID().toString();
        HttpSession newSession = request.getSession();
        newSession.setAttribute(key, user.getHiddenCaptcha());
        user.setHiddenCaptcha(key);

        if (email.trim().equals("")) {
            model.addAttribute("error", "email can not be empty \n Enter Current Email that you register with");
            return "account/management/verification-email/verification-token-email-page";
        }
        if (!userService.isUserPresentWithEmail(email)) {
            model.addAttribute("error", "email not found");
            return "account/management/verification-email/verification-token-email-page";
        }
        User userByEmail = userService.findUserByEmail(email);
        if (userService.doesUserHaveEmailToken(userByEmail)) {
            model.addAttribute("error", "verifyEmailSendToYourEmail and not Expired yet. if you didnt receive any email try it 10 min later.");
            return "account/management/verification-email/verification-token-email-page";
        }
        //userService.createVerificationToken(userByEmail, UUID.randomUUID().toString());
        User userRegistered = userService.sendVerifyEmail(userByEmail, request);
        model.addAttribute("userSuccess", userRegistered);
        newSession.removeAttribute(hideCaptcha);
        return "account/management/verification-email/verification-token-email-success-page";
    }


    @GetMapping("/verificationRegisterConfirm")
    public String confirmRegistration
            (Model model, @RequestParam("token") String token) {

        //Locale locale = request.getLocale();

        EmailVerificationToken emailVerificationToken = userService.getVerificationToken(token);
        if (emailVerificationToken == null) {
            //String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", "auth.message.invalidToken");
            //return "redirect:/badUser.html?lang=" + locale.getLanguage();
            return "account/management/verification-email/verification-register-confirm-page";
        }

        User user = emailVerificationToken.getUser();
        //Calendar cal = Calendar.getInstance();
        if ((emailVerificationToken.getExpiryDate().before(new Date()))) {

            //String messageValue = messages.getMessage("auth.message.expired", null, locale)
            model.addAttribute("message", "auth.message.expired");
            model.addAttribute("expireDate", emailVerificationToken.getExpiryDate());
            emailVerificationRepository.deleteById(emailVerificationToken.getId());
            //return "redirect:/badUser.html?lang=" + locale.getLanguage();
            return "account/management/verification-email/verification-register-confirm-page";
        }

        user.setEnable(true);
        userService.saveRegisteredUser(user);
        emailVerificationRepository.deleteById(emailVerificationToken.getId());
        final String message = "?saveUserSuccess=true";
        return "redirect:/login" + message;
    }

    private void getCaptcha(User user) {
        Captcha captcha = CaptchaUtil.createCaptcha(220, 70);
        user.setHiddenCaptcha(captcha.getAnswer());
        user.setCaptcha(""); // value entered by the User
        user.setRealCaptcha(CaptchaUtil.encodeCaptcha(captcha));
    }


}
