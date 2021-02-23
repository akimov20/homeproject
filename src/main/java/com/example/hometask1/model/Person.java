package com.example.hometask1.model;

import com.example.hometask1.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Person extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "middle_name")
    private String patronymic;
    @Column(name = "birth_date")
    @Temporal(value = TemporalType.DATE)
    private Date birthday;
    @Transient
    private List<Book> bookList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL)
    @Where(clause = "return_date is null")
    @JsonIgnore
    private List<LibraryCard> cards = new ArrayList<>();

    public List<Book> getBookList() {
        cards.forEach(libraryCard -> {
            Book book = libraryCard.getBook();
            bookList.add(book);
        });
        return bookList;
    }

    public void addBook(Book book) {
        this.bookList.add(book);
    }

}
