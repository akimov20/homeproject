package com.example.hometask1.controller;

import com.example.hometask1.dto.GenreDto;
import com.example.hometask1.model.Genre;
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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/clear-data-script.sql", "/sql/insert-data-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/clear-data-script.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GenreControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getGenreList() {
        ResponseEntity<List<GenreDto>> response = restTemplate.exchange("/genres", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<GenreDto>>() {
                });

        List<GenreDto> genres = response.getBody();

        assertThat(genres, hasSize(2));
        assertThat(genres.get(0).getId(), is(1L));
        assertThat(genres.get(0).getName(), is("dram"));
        assertThat(genres.get(1).getId(), is(2L));
        assertThat(genres.get(1).getName(), is("novel"));
    }

    @Test
    void removeGenre() {
        final Long genreId = 2L;
        ResponseEntity response = restTemplate.exchange("/genres/{id}",
                HttpMethod.DELETE, null, Long.class, genreId);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void addGenre() {
        Genre genre = new Genre();
        genre.setName("horror");
        ResponseEntity<GenreDto> response = restTemplate.postForEntity("/genres/add", genre, GenreDto.class);
        GenreDto genreDto = response.getBody();

        final Long expectedId = 20L;
        assertThat(genreDto.getName(), is("horror"));
        assertThat(genreDto.getId(), is(expectedId));
    }

    @Test
    void getStatistic() {
        ResponseEntity<List<GenreDto>> response = restTemplate.exchange("/genres/statistic", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<GenreDto>>() {
                });

        List<GenreDto> genres = response.getBody();

        assertThat(genres, hasSize(2));
        assertThat(genres.get(0).getId(), is(1L));
        assertThat(genres.get(0).getName(), is("dram"));
        assertThat(genres.get(0).getCount(), is(3));
        assertThat(genres.get(1).getId(), is(2L));
        assertThat(genres.get(1).getName(), is("novel"));
        assertThat(genres.get(1).getCount(), is(1));
    }
}