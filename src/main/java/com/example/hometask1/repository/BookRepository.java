package com.example.hometask1.repository;

import com.example.hometask1.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
    Book findByName(String name);
}
