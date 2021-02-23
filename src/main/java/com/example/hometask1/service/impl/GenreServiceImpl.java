package com.example.hometask1.service.impl;

import com.example.hometask1.aspects.annotation.CrudOperation;
import com.example.hometask1.dto.GenreDto;
import com.example.hometask1.dto.mapper.GenreMapper;
import com.example.hometask1.model.Book;
import com.example.hometask1.model.Genre;
import com.example.hometask1.repository.GenreRepository;
import com.example.hometask1.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.DELETE)
    public void removeGenre(Long id) {
        Optional<Genre> genreOptional = genreRepository.findById(id);
        if (genreOptional.isPresent()) {
            Genre genre = genreOptional.get();
            for (Book book : genre.getBooks()) {
                book.getGenres().removeIf(b -> b.getId().equals(genre.getId()));
            }
            genreRepository.deleteById(genre.getId());
        }
    }

    public List<GenreDto> getGenreList() {
        List<GenreDto> genres = new ArrayList<>();
        Iterator<Genre> iterator = genreRepository.findAll().iterator();
        while (iterator.hasNext()) {
            Genre genre = iterator.next();
            genres.add(genreMapper.createGenreDto(genre));
        }
        return genres;
    }

    @CrudOperation(operation = CrudOperation.Operation.INSERT)
    public GenreDto addGenre(Genre genre) {
        return genreMapper.createGenreDto(genreRepository.save(genre));
    }


    public List<GenreDto> getStatistic() {
        List<GenreDto> genres = new ArrayList<>();
        Iterator<Genre> iterator = genreRepository.findAll().iterator();
        while (iterator.hasNext()) {
            Genre genre = iterator.next();
            genres.add(genreMapper.createGenreWithStatisticDto(genre));
        }
        return genres;
    }

}

