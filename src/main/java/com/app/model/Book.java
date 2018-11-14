package com.app.model;


import com.app.model.security.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Transient
    private MultipartFile multipartFile;
    private String photo;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private Author author;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = CascadeType.REMOVE, mappedBy = "books")
    private Set<User> users = new HashSet<>();
}
