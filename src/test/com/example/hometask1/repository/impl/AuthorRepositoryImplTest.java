package com.example.hometask1.repository.impl;

import com.example.hometask1.model.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/insert-data-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/clear-data-script.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthorRepositoryImplTest {
    @Autowired
    private AuthorRepositoryImpl authorRepository;

    private final String firstName1 = "Lev";
    private final String lastName1 = "Tolstoy";
    private final String patronymic1 = "Nikolaevich";

    private final String firstName2 = "Fedor";
    private final String lastName2 = "Dostoevsky";
    private final String patronymic2 = "Mihailovich";

    @Test
    void findAuthorsByFullName() {
        List<Author> result = authorRepository.findAuthors(firstName1, lastName1, patronymic1, null, null);
        Author author = result.get(0);

        assertEquals(1, result.size());
        assertEquals(firstName1, author.getFirstName());
        assertEquals(lastName1, author.getLastName());
        assertEquals(patronymic1, author.getPatronymic());
    }

    @Test
    void findAuthorsByDateFrom() {
        LocalDate from = LocalDate.of(2000, 1, 1);

        List<Author> result = authorRepository.findAuthors(null, null, null, from, null);
        Author author = result.get(0);

        assertEquals(1, result.size());
        assertEquals(firstName2, author.getFirstName());
        assertEquals(lastName2, author.getLastName());
        assertEquals(patronymic2, author.getPatronymic());
    }

    @Test
    void findAuthorsByDateTo() {
        LocalDate to = LocalDate.of(2000, 1, 1);

        List<Author> result = authorRepository.findAuthors(null, null, null, null, to);
        Author author = result.get(0);

        assertEquals(1, result.size());
        assertEquals(firstName1, author.getFirstName());
        assertEquals(lastName1, author.getLastName());
        assertEquals(patronymic1, author.getPatronymic());
    }


    @Test
    void findAuthorsByDateBetween() {
        LocalDate to = LocalDate.of(2050, 1, 1);
        LocalDate from = LocalDate.of(1750, 1, 1);

        List<Author> result = authorRepository.findAuthors(null, null, null, from, to);
        assertEquals(2, result.size());
        assertEquals(firstName1, result.get(0).getFirstName());
        assertEquals(lastName1, result.get(0).getLastName());
        assertEquals(patronymic1, result.get(0).getPatronymic());
        assertEquals(firstName2, result.get(1).getFirstName());
        assertEquals(lastName2, result.get(1).getLastName());
        assertEquals(patronymic2, result.get(1).getPatronymic());
    }

    @Test
    void findAuthorsByAllNullParameters() {
        List<Author> result = authorRepository.findAuthors(null, null, null, null, null);
        assertEquals(2, result.size());
        assertEquals(firstName1, result.get(0).getFirstName());
        assertEquals(lastName1, result.get(0).getLastName());
        assertEquals(patronymic1, result.get(0).getPatronymic());
        assertEquals(firstName2, result.get(1).getFirstName());
        assertEquals(lastName2, result.get(1).getLastName());
        assertEquals(patronymic2, result.get(1).getPatronymic());
    }
}