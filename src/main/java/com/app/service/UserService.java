package com.app.service;


import com.app.exceptions.MyException;
import com.app.model.Book;
import com.app.model.security.Role;
import com.app.model.security.User;
import com.app.repositories.BookRepository;
import com.app.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private BookRepository bookRepository;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookRepository = bookRepository;
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User getUser(String userName) {
        return userRepository.findByUsername(userName);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<Book> getAllBooksByUser(User user) {
        try {
            return user.getBooks()
                    .stream()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException("SERVICE ALL BOOKS BY USER EXCEPTION", LocalDateTime.now());
        }
    }

    public List<Book> getAllBooksWhichUserDoesNotHave(User user) {
        try {
            List<Book> userBooks = user.getBooks().stream().collect(Collectors.toList());
            return bookRepository.findAll().stream().filter(x -> (!userBooks.contains(x)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException("SERVICE ALL BOOKS BY USER EXCEPTION", LocalDateTime.now());
        }
    }

}
