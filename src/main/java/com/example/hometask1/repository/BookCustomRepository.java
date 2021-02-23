package com.example.hometask1.repository;

import com.example.hometask1.model.Book;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookCustomRepository {
    List<Book> findBooks(String genreName, Integer year, String filter) throws IllegalArgumentException;
}
