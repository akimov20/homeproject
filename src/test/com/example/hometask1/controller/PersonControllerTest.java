package com.example.hometask1.controller;

import com.example.hometask1.config.TestWebSecurityConfig;
import com.example.hometask1.dto.DebtorDto;
import com.example.hometask1.dto.PersonDto;
import com.example.hometask1.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/clear-data-script.sql", "/sql/insert-data-script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/clear-data-script.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PersonControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getBookListByPerson() {
        final Long id = 1L;
        ResponseEntity<PersonDto> response = restTemplate.exchange("/persons/{id}/books", HttpMethod.GET, null,
                new ParameterizedTypeReference<PersonDto>() {
                }, id);

        PersonDto result = response.getBody();

        assertThat(result.getId(), is(id));
        assertThat(result.getFirstName(), is("Vladimir"));
        assertThat(result.getLastName(), is("Akimov"));
        assertThat(result.getPatronymic(), is("Alexandrovich"));
        assertThat(result.getBookList(), hasSize(1));
        assertThat(result.getBookList().get(0).getId(), is(1L));
    }

    @Test
    void removePerson() {
        final Long personId = 4L;
        ResponseEntity response = restTemplate.exchange("/persons/{id}",
                HttpMethod.DELETE, null, Long.class, personId);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void removePersonByFullName() {
        final String fullName = "Akimov Andrey Alexandrovich";
        ResponseEntity response = restTemplate.exchange("/persons/byName/{fullName}",
                HttpMethod.DELETE, null, String.class, fullName);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void takeBook() {
        Long personId = 2L;
        Long bookId = 2L;
        ResponseEntity<PersonDto> response = restTemplate.exchange("/persons/{id}/books/add/{bookId}",
                HttpMethod.PUT, null, new ParameterizedTypeReference<PersonDto>() {
                }, personId, bookId);

        PersonDto result = response.getBody();

        assertThat(result.getId(), is(personId));
        assertThat(result.getFirstName(), is("Sergey"));
        assertThat(result.getLastName(), is("Dmitryev"));
        assertThat(result.getPatronymic(), is("Romanovich"));
        assertThat(result.getBookList(), hasSize(1));
        assertThat(result.getBookList().get(0).getId(), is(bookId));
    }

    @Test
    void removeBook() {
        Long personId = 1L;
        Long bookId = 1L;
        ResponseEntity<PersonDto> response = restTemplate.exchange("/persons/{id}/books/remove/{bookId}",
                HttpMethod.DELETE, null, new ParameterizedTypeReference<PersonDto>() {
                }, personId, bookId);

        PersonDto result = response.getBody();

        assertThat(result.getId(), is(personId));
        assertThat(result.getFirstName(), is("Vladimir"));
        assertThat(result.getLastName(), is("Akimov"));
        assertThat(result.getPatronymic(), is("Alexandrovich"));
        assertThat(result.getBookList(), hasSize(0));
    }

    @Test
    void addPerson() {
        Person person = new Person();
        person.setFirstName("Dmitry");
        person.setLastName("Sergeev");

        ResponseEntity<PersonDto> response = restTemplate.postForEntity("/persons/add", person, PersonDto.class);
        PersonDto personDto = response.getBody();

        final Long expectedId = 20L;
        assertThat(personDto.getFirstName(), is("Dmitry"));
        assertThat(personDto.getLastName(), is("Sergeev"));
        assertThat(personDto.getId(), is(expectedId));
    }

    @Test
    void updatePerson() {
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Dmitry");
        person.setLastName("Sergeev");
        person.setPatronymic("Vladimirovich");

        HttpEntity<Person> entity = new HttpEntity<>(person);
        ResponseEntity<PersonDto> response = restTemplate.exchange("/persons/update", HttpMethod.POST, entity,
                new ParameterizedTypeReference<PersonDto>() {
                });

        PersonDto result = response.getBody();

        assertThat(result.getId(), is(1L));
        assertThat(result.getFirstName(), is("Dmitry"));
        assertThat(result.getLastName(), is("Sergeev"));
        assertThat(result.getPatronymic(), is("Vladimirovich"));
    }

    @Test
    void externBook() {
        final Long personId = 1L;
        final Long bookId = 1L;
        final int days = 7;
        final String url = "/persons/extern/{personId}/{bookId}/{days}";
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class, personId, bookId, days);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getDebtors() {
        ResponseEntity<List<DebtorDto>> response = restTemplate.exchange("/persons/debtors",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<DebtorDto>>() {
                });
        List<DebtorDto> debtors = response.getBody();
        DebtorDto debtorDto = debtors.get(0);
        final String personInfo = "Romanov Andrey Konstantinovich";

        assertThat(debtors, hasSize(1));
        assertThat(debtorDto.getPersonInfo(), is(personInfo));
        assertThat(debtorDto.getBookInfo(), is("War and Peace"));
    }
}