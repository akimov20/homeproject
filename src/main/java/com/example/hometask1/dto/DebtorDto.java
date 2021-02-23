package com.example.hometask1.dto;

import com.example.hometask1.model.Book;
import com.example.hometask1.model.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DebtorDto {
    @JsonProperty("person")
    private String personInfo;
    @JsonProperty("book")
    private String bookInfo;
    @JsonProperty("delinquency")
    private int delinquency;

    public DebtorDto(Person person, Book book, int delinquency) {
        this.bookInfo = book.getName();
        this.personInfo = person.getLastName().concat(" " + person.getFirstName()).concat(" " + person.getPatronymic());
        this.delinquency = delinquency;
    }
}
