package com.example.hometask1.service;

import com.example.hometask1.dto.GenreDto;
import com.example.hometask1.model.Genre;

import java.util.List;

public interface GenreService {
    void removeGenre(Long id);

    List<GenreDto> getGenreList();

    GenreDto addGenre(Genre genre);

    List<GenreDto> getStatistic();
}
