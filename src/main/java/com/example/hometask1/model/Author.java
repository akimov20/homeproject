package com.example.hometask1.model;

import com.example.hometask1.model.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "author")
@Data
@NoArgsConstructor
@ToString(of = {"id", "firstName", "lastName", "patronymic", "createdDate"})
@EqualsAndHashCode(exclude = "bookList")
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Book> bookList;

}
