package com.app.controllers;


import com.app.model.Book;
import com.app.model.security.Role;
import com.app.model.security.User;
import com.app.service.BookService;
import com.app.service.UserService;
import com.app.validators.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private UserService userService;
    private UserValidator userValidator;
    private BookService bookService;


    public UserController(UserService userService, UserValidator userValidator, BookService bookService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.bookService = bookService;
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(userValidator);
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        model.addAttribute("errors", new HashMap<>());
        return "security/register";
    }

    @PostMapping("/register")
    public String registerPOST(
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode, (v1, v2) -> v1 + ", " + v2)));
            return "security/register";
        }

        userService.registerUser(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", "");
        return "security/loginForm";
    }

    @GetMapping("/userDash")
    public String userDash() {
        return "user/userDash";
    }


    @GetMapping("/adminDash")
    public String adminDash() {
        return "admin/adminDash";
    }


    @GetMapping("/favouriteBooks")
    public String favouriteBooks(Model model, Principal principal) {
        model.addAttribute("books", userService.getAllBooksByUser(userService.getUser(principal.getName())));
        return "user/favouriteBooks";
    }

    @GetMapping("/addFavouriteBook")
    public String addToFavouriteBooks(Model model, Principal principal) {
        model.addAttribute("books", userService.getAllBooksWhichUserDoesNotHave(userService.getUser(principal.getName())));
        return "user/addToFavouriteBooks";
    }

    @PostMapping("/addFavouriteBook")
    public String addToFavouriteBooksPost(@RequestParam Long id, Principal principal) {
        Book b = bookService.getBook(id);
        User user = userService.getUser(principal.getName());
        user.getBooks().add(b);
        userService.saveUser(user);
        return "redirect:/favouriteBooks";
    }


    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("error", "NIEPRAWIDŁOWE DANE LOGOWANIA");
        return "security/loginForm";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(Model model) {
        return "security/accessDenied";
    }

}


