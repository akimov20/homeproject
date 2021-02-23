package com.example.hometask1.service;

import com.example.hometask1.dto.PersonDto;
import com.example.hometask1.model.Person;

import java.util.List;

public interface PersonService {
    PersonDto addPerson(Person person);

    PersonDto updatePerson(Person person);

    boolean removePerson(Long id);

    boolean removePersonByFullName(String fullName);

    PersonDto getBookList(Long id);

    List<Person> getPersons();

    PersonDto takeBook(Long id, Long bookId);

    void returnBook(Long id, Long bookId);
}
