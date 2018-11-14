package com.app.controllers;

import com.app.model.Author;
import com.app.service.AuthorService;
import com.app.validators.AuthorValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private AuthorService authorService;
    private AuthorValidator authorValidator;

    public AuthorController(AuthorService authorService, AuthorValidator authorValidator) {
        this.authorService = authorService;
        this.authorValidator = authorValidator;
    }


    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(authorValidator);
    }

    @GetMapping
    public String getAllAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "authors/all";
    }

    @GetMapping("/add")
    public String addAuthor(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("errors", new HashMap<>());
        return "authors/add";
    }

    @PostMapping("/add")
    public String addAuthorPost(@Valid @ModelAttribute Author author, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("author", author);
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode, (v1, v2) -> v1 + ", " + v2)));
            return "authors/add";
        }
        authorService.addAuthor(author);
        return "redirect:/authors";
    }

    @GetMapping("/edit/{id}")
    public String editAuthor(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.getAuthor(id));
        return "authors/edit";
    }

    @PostMapping("/edit")
    public String editAuthorPost(@ModelAttribute Author author) {
        authorService.updateAuthor(author);
        return "redirect:/authors";
    }

    @PostMapping("/remove")
    public String removeAuthorPost(@RequestParam Long id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }

    @GetMapping("/details/{id}")
    public String detailsAuthor(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.getAuthor(id));
        return "authors/details";
    }

}
