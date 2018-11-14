package com.app.validators;


import com.app.exceptions.MyException;
import com.app.model.security.User;
import com.app.repositories.UserRepository;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(User.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            User user = (User) o;

            if (userRepository.findByUsername(user.getUsername()) != null) {
                errors.rejectValue("username", "USER ALREADY EXISTS");
            }

            if (!Objects.equals(user.getPassword(), user.getPasswordConfirmation())) {
                errors.rejectValue("password", "PASSWORD AND PASSWORD CONFIRMATION MUST BE EQUAL");
            }

            if (!EmailValidator.getInstance().isValid(user.getEmail())) {
                errors.rejectValue("email", "EMAIL IS NOT VALID");
            }

            if (userRepository.findByEmail(user.getEmail()) != null) {
                errors.rejectValue("email", "EMAIL ALREADY REGISTERED");
            }

        } catch (Exception e) {
            throw new MyException("REGISTER VALIDATION EXCEPTION", LocalDateTime.now());
        }
    }
}
