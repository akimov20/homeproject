package com.example.hometask1.service.impl;

import com.example.hometask1.dto.GenreDto;
import com.example.hometask1.dto.mapper.GenreMapper;
import com.example.hometask1.model.Genre;
import com.example.hometask1.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {
    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreServiceImpl genreService;

    private Genre genre;
    private GenreDto genreDto;
    private Long id = 1L;

    @BeforeEach
    private void init() {
        genre = new Genre();
        genre.setId(id);
        genre.setName("novel");

        genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
    }

    @Test
    void removeGenre() {
        Mockito.when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        genreService.removeGenre(id);
        Mockito.verify(genreRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void getGenreList() {
        List<Genre> list = new ArrayList<>();
        list.add(genre);
        Mockito.when(genreRepository.findAll()).thenReturn(list);
        Mockito.when(genreMapper.createGenreDto(genre)).thenReturn(genreDto);

        List<GenreDto> results = genreService.getGenreList();
        assertEquals(1, results.size());
        Mockito.verify(genreRepository, Mockito.times(1)).findAll();
    }

    @Test
    void addGenre() {
        Mockito.when(genreRepository.save(genre)).thenReturn(genre);
        Mockito.when(genreMapper.createGenreDto(genre)).thenReturn(genreDto);

        GenreDto result = genreService.addGenre(genre);
        assertEquals(genreDto.getId(), result.getId());
        assertEquals(genreDto.getName(), result.getName());
        Mockito.verify(genreRepository, Mockito.times(1)).save(genre);
    }

    @Test
    void getStatistic() {
        final int count = 5;
        genre.setCount(count);
        genreDto.setCount(count);

        List<Genre> list = new ArrayList<>();
        list.add(genre);
        Mockito.when(genreRepository.findAll()).thenReturn(list);
        Mockito.when(genreMapper.createGenreWithStatisticDto(genre)).thenReturn(genreDto);

        List<GenreDto> results = genreService.getStatistic();
        assertEquals(1, results.size());
        Mockito.verify(genreRepository, Mockito.times(1)).findAll();
        assertEquals(count, results.get(0).getCount());
    }
}