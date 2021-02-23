package com.example.hometask1.dto.mapper;

import com.example.hometask1.dto.PersonDto;
import com.example.hometask1.model.Person;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = {BookMapper.class})
public interface PersonMapper {

    @Named("persons")
    @InheritInverseConfiguration
    @Mapping(target = "bookList", ignore = true, qualifiedByName = "booksWithoutAuthor")
    PersonDto createPersonDto(Person person);

    @Named("personsWithBooks")
    @InheritInverseConfiguration
    @Mapping(target = "bookList", qualifiedByName = "books")
    PersonDto createPersonWithBooksDto(Person person);

}