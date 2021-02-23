package com.example.hometask1.dto;

import com.example.hometask1.jsonview.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GenreDto {
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class, View.BookView.class})
    private Long id;
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class, View.BookView.class})
    private String name;
    private int count;
}
