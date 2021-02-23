package com.example.hometask1.dto.mapper;

import com.example.hometask1.dto.GenreDto;
import com.example.hometask1.model.Genre;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    @Named("genres")
    @InheritInverseConfiguration
    @Mapping(target = "count", ignore = true)
    GenreDto createGenreDto(Genre genre);

    @InheritInverseConfiguration
    GenreDto createGenreWithStatisticDto(Genre genre);
}
