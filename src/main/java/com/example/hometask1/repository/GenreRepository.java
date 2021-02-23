package com.example.hometask1.repository;

import com.example.hometask1.model.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Genre findByName(String name);
}
