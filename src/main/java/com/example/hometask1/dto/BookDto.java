package com.example.hometask1.dto;

import com.example.hometask1.jsonview.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BookDto {
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class, View.BookView.class})
    private Long id;
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class, View.BookView.class})
    private String name;
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class})
    private AuthorDto author;
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class, View.BookView.class})
    private Set<GenreDto> genres = new HashSet<>();
}
