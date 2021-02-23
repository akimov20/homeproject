package com.example.hometask1.model;

import com.example.hometask1.model.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@ToString(of = {"id", "name", "genres"})
@EqualsAndHashCode(exclude = "genres")
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "admission_to_library_date")
    private LocalDateTime admissionDate;
    @Column(name = "publication_date")
    private LocalDateTime publicationDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "book_genre_link",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "book")
    private List<LibraryCard> cards = new ArrayList<>();

    @Transient
    private List<Person> persons = new ArrayList<>();

    public List<Person> getPersons() {
        for (LibraryCard card : cards) {
            persons.add(card.getPerson());
        }
        return persons;
    }

    public void addPerson(Person person) {
        persons.add(person);
    }
}
