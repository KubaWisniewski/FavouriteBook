package com.app.service;

import com.app.exceptions.MyException;
import com.app.model.Book;
import com.app.repositories.BookRepository;
import com.app.utils.FileManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private FileManager fileManager;
    private BookRepository bookRepository;

    public BookService(FileManager fileManager, BookRepository bookRepository) {
        this.fileManager = fileManager;
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        try {
            String filename = fileManager.addFile(book.getMultipartFile());
            book.setPhoto(filename);
            bookRepository.save(book);
        } catch (Exception e) {
            throw new MyException("SERVICE ADD BOOK EXCEPTION", LocalDateTime.now());
        }
    }

    public void updateBook(Book book) {
        try {
            fileManager.updateFile(book.getMultipartFile(), book.getPhoto());
            bookRepository.save(book);
        } catch (Exception e) {
            throw new MyException("SERVICE UPDATE BOOK EXCEPTION", LocalDateTime.now());
        }
    }

    public void deleteBook(Long id) {
        try {
            Book book = bookRepository.findById(id).orElseThrow(NullPointerException::new);
            fileManager.removeFile(book.getPhoto());
            bookRepository.delete(book);
        } catch (Exception e) {
            throw new MyException("SERVICE DELETE BOOK EXCEPTION", LocalDateTime.now());
        }
    }

    public List<Book> getAllBooks() {
        try {
            return bookRepository
                    .findAll()
                    .stream()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException("SERVICE ALL BOOKS EXCEPTION", LocalDateTime.now());
        }
    }

    public Book getBook(Long id) {
        try {
            return bookRepository.findById(id).orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            throw new MyException("SERVICE GET ONE BOOK EXCEPTION", LocalDateTime.now());
        }
    }

}
