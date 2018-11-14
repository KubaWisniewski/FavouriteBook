package com.app.model.security;


import com.app.model.Book;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    @Transient
    private String passwordConfirmation;
    private String email;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "user_book",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "book_id")
            }
    )
    private Set<Book> books = new HashSet<>();
}
