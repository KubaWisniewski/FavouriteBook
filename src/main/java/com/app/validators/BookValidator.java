package com.app.validators;

import com.app.exceptions.MyException;
import com.app.model.Author;
import com.app.model.Book;
import com.app.repositories.AuthorRepository;
import com.app.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import java.time.LocalDateTime;

@Component
public class BookValidator implements Validator {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Book.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            Book book = (Book) o;

            if (bookRepository.findBooksByTitle(book.getTitle()) != null) {
                errors.rejectValue("title", "BOOK ALREADY EXISTS");
            }
            if (!book.getTitle().matches("[A-Za-z\\s?]+")) {
                errors.rejectValue("title", "BOOK TITLE IS NOT CORRECT");
            }

        } catch (Exception e) {
            throw new MyException("BOOK VALIDATION EXCEPTION", LocalDateTime.now());
        }
    }
}
