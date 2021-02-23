package com.example.hometask1.repository.impl;

import com.example.hometask1.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/insert-data-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/clear-data-script.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookRepositoryImplTest {

    @Autowired
    private BookRepositoryImpl bookRepository;

    @Test
    void findBooksByGenreName() {
        String genreName = "novel";
        List<Book> result = bookRepository.findBooks(genreName, null, null);
        Book book = result.get(0);

        assertEquals(1, result.size());
        assertEquals("War and Peace", book.getName());
        assertEquals(1, book.getId());
    }

    @Test
    void findBooksByFilterBigger() {
        String filter = "bigger";
        int year = 2020;
        List<Book> result = bookRepository.findBooks(null, year, filter);
        Book book = result.get(0);

        assertEquals(1, result.size());
        assertEquals("Crime and Punishment", book.getName());
        assertEquals(3, book.getId());
        assertEquals(2054, book.getPublicationDate().getYear());
    }

    @Test
    void findBooksByFilterLess() {
        String filter = "less";
        int year = 1885;
        List<Book> result = bookRepository.findBooks(null, year, filter);
        Book book = result.get(0);

        assertEquals(1, result.size());
        assertEquals("War and Peace", book.getName());
        assertEquals(1, book.getId());
        assertEquals(1880, book.getPublicationDate().getYear());
    }

    @Test
    void findBooksByFilterEquals() {
        String filter = "equals";
        int year = 1888;
        List<Book> result = bookRepository.findBooks(null, year, filter);
        Book book = result.get(0);

        assertEquals(1, result.size());
        assertEquals("Idiot", book.getName());
        assertEquals(2, book.getId());
        assertEquals(1888, book.getPublicationDate().getYear());
    }

    @Test
    void findBooksByFilterException() {
        int year = 2020;
        String genreName = "novel";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bookRepository.findBooks(genreName, year, null);
        });
    }
}