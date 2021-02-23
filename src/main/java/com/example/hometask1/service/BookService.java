package com.example.hometask1.service;

import com.example.hometask1.dto.BookDto;
import com.example.hometask1.model.Book;

import java.util.List;

public interface BookService {
    List<BookDto> getBookByGenre(String name);

    List<BookDto> getBookByAuthor(String firstName, String lastName, String patronymic);

    BookDto addBook(Book book);

    BookDto updateGenre(Book book);

    boolean removeBook(Long id);

    List<BookDto> searchBooks(String genreName, Integer year, String filter);
}
