package com.example.hometask1.dto;

import com.example.hometask1.jsonview.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class AuthorDto {
    @JsonView(value = {View.AuthorView.class, View.BookView.class})
    private Long id;
    @JsonView(value = {View.AuthorView.class, View.BookView.class})
    private String firstName;
    @JsonView(value = {View.AuthorView.class, View.BookView.class})
    private String lastName;
    @JsonView(value = {View.AuthorView.class, View.BookView.class})
    private String patronymic;
    @JsonView(value = {View.BookView.class})
    private List<BookDto> bookList;
}
