package com.example.hometask1.dto;

import com.example.hometask1.jsonview.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PersonDto {
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class})
    private Long id;
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class})
    private String firstName;
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class})
    private String lastName;
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class})
    private String patronymic;
    @JsonView(value = {View.MainInfoView.class, View.AuthorView.class})
    private Date birthday;
    @JsonView(value = {View.AuthorView.class})
    private List<BookDto> bookList;
}
