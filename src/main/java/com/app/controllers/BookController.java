package com.app.controllers;

import com.app.model.Author;
import com.app.model.Book;
import com.app.service.AuthorService;
import com.app.service.BookService;
import com.app.validators.BookValidator;
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
@RequestMapping("/books")
public class BookController {

    private BookService bookService;
    private AuthorService authorService;
    private BookValidator bookValidator;

    public BookController(BookService bookService, AuthorService authorService, BookValidator bookValidator) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.bookValidator = bookValidator;
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(bookValidator);
    }

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books/all";
    }

    @GetMapping("/add")
    public String addBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("errors", new HashMap<>());
        return "books/add";
    }

    @PostMapping("/add")
    public String addBookPost(@Valid @ModelAttribute Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode, (v1, v2) -> v1 + ", " + v2)));
            return "books/add";
        }
        Author a = authorService.getAuthor(book.getAuthor().getId());
        book.setAuthor(a);
        bookService.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBook(id));
        model.addAttribute("authors", authorService.getAllAuthors());
        return "books/edit";
    }

    @PostMapping("/edit")
    public String editBookPost(@ModelAttribute Book book) {
        Author a = authorService.getAuthor(book.getAuthor().getId());
        book.setAuthor(a);
        bookService.updateBook(book);
        return "redirect:/books";
    }

    @PostMapping("/remove")
    public String removeBookPost(@RequestParam Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/details/{id}")
    public String detailsBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBook(id));
        return "books/details";
    }
}




