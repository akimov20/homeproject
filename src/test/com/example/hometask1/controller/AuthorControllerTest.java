package com.example.hometask1.controller;

import com.example.hometask1.dto.AuthorDto;
import com.example.hometask1.dto.BookDto;
import com.example.hometask1.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/clear-data-script.sql", "/sql/insert-data-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/clear-data-script.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthorControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAuthorList() {
        ResponseEntity<List<AuthorDto>> response = restTemplate.exchange("/authors", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuthorDto>>() {
                });

        List<AuthorDto> authors = response.getBody();

        assertThat(authors, hasSize(2));
        assertThat(authors.get(0).getId(), is(1L));
        assertThat(authors.get(0).getFirstName(), is("Lev"));
        assertThat(authors.get(0).getLastName(), is("Tolstoy"));
        assertThat(authors.get(0).getPatronymic(), is("Nikolaevich"));
        assertThat(authors.get(1).getId(), is(2L));
        assertThat(authors.get(1).getFirstName(), is("Fedor"));
        assertThat(authors.get(1).getLastName(), is("Dostoevsky"));
        assertThat(authors.get(1).getPatronymic(), is("Mihailovich"));
    }

    @Test
    void getAuthorsWithBooks() {
        Long authorId = 1L;
        ResponseEntity<AuthorDto> response = restTemplate.exchange("/authors/{id}", HttpMethod.GET, null,
                new ParameterizedTypeReference<AuthorDto>() {
                }, authorId);

        AuthorDto author = response.getBody();
        BookDto bookDto = author.getBookList().get(0);

        assertThat(author.getId(), is(authorId));
        assertThat(author.getFirstName(), is("Lev"));
        assertThat(author.getLastName(), is("Tolstoy"));
        assertThat(author.getPatronymic(), is("Nikolaevich"));
        assertThat(author.getBookList(), hasSize(1));
        assertThat(bookDto.getName(), is("War and Peace"));
    }

    @Test
    void removeAuthorCascadeMethod() {
        final Long authorId = 2L;
        ResponseEntity response = restTemplate.exchange("/authors/{id}/cascade", HttpMethod.DELETE,
                null, String.class, authorId);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void removeAuthor() {
        final Long authorId = 2L;
        ResponseEntity response = restTemplate.exchange("/authors/{id}", HttpMethod.DELETE,
                null, String.class, authorId);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().toString(), is("Author is not removed. Author has books"));
    }

    @Test
    void addAuthor() {
        Author author = new Author();
        author.setFirstName("Vladimir");
        author.setLastName("Mayakovsky");
        ResponseEntity<AuthorDto> response = restTemplate.postForEntity("/authors/add", author, AuthorDto.class);
        AuthorDto authorDto = response.getBody();

        final Long expectedId = 20L;
        assertThat(authorDto.getFirstName(), is("Vladimir"));
        assertThat(authorDto.getLastName(), is("Mayakovsky"));
        assertThat(authorDto.getId(), is(expectedId));
    }

    @Test
    void searchAuthors() {
        final String url = "/authors/search?firstName={firstName}&lastName={lastName}&patronymic={patronymic}";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("firstName", "Fedor");
        parameters.put("lastName", "Dostoevsky");
        parameters.put("patronymic", "Mihailovich");

        ResponseEntity<List<AuthorDto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuthorDto>>() {
                }, parameters);

        List<AuthorDto> authors = response.getBody();

        assertThat(authors, hasSize(1));
        assertThat(authors.get(0).getId(), is(2L));
        assertThat(authors.get(0).getFirstName(), is("Fedor"));
        assertThat(authors.get(0).getLastName(), is("Dostoevsky"));
        assertThat(authors.get(0).getPatronymic(), is("Mihailovich"));
    }
}