package com.app.service;


import com.app.exceptions.MyException;
import com.app.model.Author;
import com.app.model.Book;
import com.app.repositories.AuthorRepository;
import com.app.repositories.BookRepository;
import com.app.utils.FileManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private FileManager fileManager;
    private AuthorRepository authorRepository;

    public AuthorService(FileManager fileManager, AuthorRepository authorRepository) {
        this.fileManager = fileManager;
        this.authorRepository = authorRepository;
    }

    public void addAuthor(Author author) {
        try {

            authorRepository.save(author);
        } catch (Exception e) {
            throw new MyException("SERVICE ADD AUTHOR EXCEPTION", LocalDateTime.now());
        }
    }

    public void updateAuthor(Author author) {
        try {

            authorRepository.save(author);
        } catch (Exception e) {
            throw new MyException("SERVICE UPDATE AUTHOR EXCEPTION", LocalDateTime.now());
        }
    }

    public void deleteAuthor(Long id) {
        try {
            Author author = authorRepository.findById(id).orElseThrow(NullPointerException::new);
            authorRepository.delete(author);
        } catch (Exception e) {
            throw new MyException("SERVICE DELETE AUTHOR EXCEPTION", LocalDateTime.now());
        }
    }

    public List<Author> getAllAuthors() {
        try {
            return authorRepository
                    .findAll()
                    .stream()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException("SERVICE ALL AUTHORS EXCEPTION", LocalDateTime.now());
        }
    }

    public Author getAuthor(Long id) {
        try {
            return authorRepository.findById(id).orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            throw new MyException("SERVICE GET ONE AUTHOR EXCEPTION", LocalDateTime.now());
        }
    }


}
