package com.app.repositories;


import com.app.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByNameAndSurname(String name, String Surname);
}
