package com.example.hometask1.repository;

import com.example.hometask1.model.Author;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findAll(Example<Author> author);

    @Override
    List<Author> findAll();
}
