package com.app.repositories;


import com.app.model.Book;
import com.app.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBooksByTitle(String title);
}
