package com.example.hometask1.dto.mapper;

import com.example.hometask1.dto.BookDto;
import com.example.hometask1.model.Book;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {
    @Named("books")
    @InheritInverseConfiguration
    @Mapping(target = "author", qualifiedByName = "authorsWithoutBooks")
    @Mapping(target = "genres", qualifiedByName = "genres")
    BookDto createBookDto(Book book);

    @Named("booksWithoutAuthor")
    @InheritInverseConfiguration
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "genres", qualifiedByName = "genres")
    BookDto createBookWithoutAuthorDto(Book book);

}
