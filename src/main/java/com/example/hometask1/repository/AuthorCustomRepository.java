package com.example.hometask1.repository;

import com.example.hometask1.model.Author;

import java.time.LocalDate;
import java.util.List;

public interface AuthorCustomRepository {
    List<Author> findAuthors(String firstName, String lastName, String patronymic, LocalDate from, LocalDate to);
}
