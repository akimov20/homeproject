package com.example.hometask1.controller;

import com.example.hometask1.dto.GenreDto;
import com.example.hometask1.jsonview.View;
import com.example.hometask1.model.Genre;
import com.example.hometask1.service.GenreService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    @JsonView(value = View.MainInfoView.class)
    public List<GenreDto> getGenreList() {
        return genreService.getGenreList();
    }

    @DeleteMapping("/{id}")
    @JsonView(value = View.MainInfoView.class)
    public ResponseEntity removeGenre(@PathVariable(name = "id") Long id) {
        genreService.removeGenre(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add")
    @JsonView(value = View.MainInfoView.class)
    public GenreDto addGenre(@RequestBody Genre genre) {
       return genreService.addGenre(genre);
    }

    @GetMapping("/statistic")
    public List<GenreDto> getStatistic() {
        return genreService.getStatistic();
    }

}

