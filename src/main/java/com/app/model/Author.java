package com.app.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Author {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private Set<Book> books = new LinkedHashSet<>();

}
