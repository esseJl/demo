package com.example.demo.validator.valid.unique_link;


import com.example.demo.annotation.valid.unique_link.ValidUniqueLink;
import org.passay.MessageResolver;
import org.passay.PropertiesMessageResolver;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class UniqueLinkConstraintValidator implements ConstraintValidator<ValidUniqueLink, String> {


    @Override
    public boolean isValid(String uniqueLink, ConstraintValidatorContext context) {
        char c[] = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '+', '=', ' ', '/'};
        for (Character c1 : c) {
            if (uniqueLink.contains(c1.toString())) {
                context.buildConstraintViolationWithTemplate("invalid.unique.link")
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
