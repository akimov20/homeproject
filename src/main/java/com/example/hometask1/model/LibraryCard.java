package com.example.hometask1.model;


import com.example.hometask1.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "library_card")
@Data
@EqualsAndHashCode(of = "id")
public class LibraryCard extends BaseEntity implements Serializable {
    @Embeddable
    @NoArgsConstructor
    @Data
    public static class Id implements Serializable {
        @Column(name = "person_person_id")
        private Long personId;
        @Column(name = "book_book_id")
        protected Long bookId;
    }
    @EmbeddedId
    private Id id = new Id();


    @ManyToOne
    @JoinColumn(name = "book_book_id", nullable = false,insertable=false, updatable=false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "person_person_id", nullable = false,insertable=false, updatable=false)
    private Person person;

    @Column(name = "return_date")
    private ZonedDateTime returnDate;

    @Column(name = "expected_return_date")
    private ZonedDateTime expectedReturnDate;

    public void setBook(Book book) {
        this.book = book;
        this.id.setBookId(book.getId());
    }

    public void setPerson(Person person) {
        this.person = person;
        this.id.setPersonId(person.getId());
    }
}
