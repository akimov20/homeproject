package com.example.hometask1.service;

import com.example.hometask1.dto.AuthorDto;
import com.example.hometask1.model.Author;

import java.time.LocalDate;
import java.util.List;

public interface AuthorService {
    boolean removeAuthorCascadeMethod(Long id);

    boolean removeAuthor(Long id);

    List<AuthorDto> getAuthorList();

    AuthorDto getAuthorWithBooks(Long id);

    AuthorDto addAuthor(Author author);

    List<AuthorDto> searchAuthors(String firstName, String lastName, String patronymic, LocalDate from, LocalDate to);
}
