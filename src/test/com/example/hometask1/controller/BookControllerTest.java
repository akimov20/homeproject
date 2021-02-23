package com.example.hometask1.controller;

import com.example.hometask1.dto.BookDto;
import com.example.hometask1.dto.GenreDto;
import com.example.hometask1.model.Author;
import com.example.hometask1.model.Book;
import com.example.hometask1.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/clear-data-script.sql", "/sql/insert-data-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/clear-data-script.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getBookListByGenre() {
        final String genreName = "novel";
        ResponseEntity<List<BookDto>> response = restTemplate.exchange("/books/genre/{name}", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<BookDto>>() {
                }, genreName);

        List<BookDto> books = response.getBody();

        assertThat(books, hasSize(1));
        assertThat(books.get(0).getId(), is(1L));
        assertThat(books.get(0).getName(), is("War and Peace"));
    }

    @Test
    void getBookListByAuthor() {
        final String url = "/books/author?firstName={firstName}&lastName={lastName}&patronymic={patronymic}";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("firstName", "Lev");
        parameters.put("lastName", "Tolstoy");
        parameters.put("patronymic", "Nikolaevich");

        ResponseEntity<List<BookDto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<BookDto>>() {
                }, parameters);

        List<BookDto> books = response.getBody();

        assertThat(books, hasSize(1));
        assertThat(books.get(0).getId(), is(1L));
        assertThat(books.get(0).getName(), is("War and Peace"));
    }

    @Test
    void addBook() {
        Author author = new Author();
        author.setFirstName("New Author");
        author.setLastName("New Author");
        Book book = new Book();
        book.setName("New Book");
        book.setAuthor(author);

        ResponseEntity<BookDto> response = restTemplate.postForEntity("/books/add", book, BookDto.class);
        BookDto bookDto = response.getBody();

        final Long expectedId = 20L;
        assertThat(bookDto.getName(), is("New Book"));
        assertThat(bookDto.getId(), is(expectedId));
    }

    @Test
    void updateGenre() {
        final Long bookId = 2L;
        final Long genreId = 2L;
        Genre genre = new Genre();
        genre.setId(genreId);
        Book book = new Book();
        book.setId(bookId);
        Set<Genre> genres = new HashSet<>(Arrays.asList(genre));
        book.setGenres(genres);

        HttpEntity<Book> entity = new HttpEntity<>(book);
        ResponseEntity<BookDto> response = restTemplate.exchange("/books/genre/update", HttpMethod.PUT, entity,
                new ParameterizedTypeReference<BookDto>() {
                });

        BookDto result = response.getBody();
        GenreDto genreDto = result.getGenres().iterator().next();

        assertThat(result.getId(), is(bookId));
        assertThat(result.getGenres().size(), is(1));
        assertThat(genreDto.getId(), is(genreId));
    }

    @Test
    void removeBook() {
        final Long bookid = 2L;
        ResponseEntity response = restTemplate.exchange("/genres/{id}",
                HttpMethod.DELETE, null, Long.class, 2);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void searchBooks() {
        final String url = "/books/search?genreName={genreName}&year={year}&filter={filter}";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("genreName", "novel");
        parameters.put("year", null);
        parameters.put("filter", null);

        ResponseEntity<List<BookDto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<BookDto>>() {
                }, parameters);

        List<BookDto> books = response.getBody();

        assertThat(books, hasSize(1));
        assertThat(books.get(0).getId(), is(1L));
        assertThat(books.get(0).getName(), is("War and Peace"));
    }
}