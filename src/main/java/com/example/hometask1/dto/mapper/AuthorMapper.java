package com.example.hometask1.dto.mapper;

import com.example.hometask1.dto.AuthorDto;
import com.example.hometask1.model.Author;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = {BookMapper.class})
public interface AuthorMapper {
    @Named("authors")
    @InheritInverseConfiguration
    @Mapping(target = "bookList", qualifiedByName = "books")
    AuthorDto createAuthorWithBooksDto(Author author);

    @Named("authorsWithoutBooks")
    @InheritInverseConfiguration
    @Mapping(target = "bookList", ignore = true, qualifiedByName = "booksWithoutAuthor")
    AuthorDto createAuthorDto(Author author);

}
