package com.app.validators;

import com.app.exceptions.MyException;
import com.app.model.Author;
import com.app.model.security.User;
import com.app.repositories.AuthorRepository;
import com.app.repositories.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class AuthorValidator implements Validator {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Author.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            Author author = (Author) o;

            if (authorRepository.findByNameAndSurname(author.getName(), author.getSurname()) != null) {
                errors.rejectValue("name", "AUTHOR ALREADY EXISTS");
                errors.rejectValue("surname", "AUTHOR ALREADY EXISTS");
            }
            if (!author.getName().matches("[A-Z][a-zA-Z]*")) {
                errors.rejectValue("name", "AUTHOR NAME IS NOT CORRECT");
            }
            if (!author.getSurname().matches("[A-Z][a-zA-Z]*")) {
                errors.rejectValue("surname", "AUTHOR SURNAME IS NOT CORRECT");
            }
        } catch (Exception e) {
            throw new MyException("AUTHOR VALIDATION EXCEPTION", LocalDateTime.now());
        }
    }
}
