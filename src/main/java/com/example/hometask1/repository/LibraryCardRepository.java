package com.example.hometask1.repository;

import com.example.hometask1.model.LibraryCard;
import com.example.hometask1.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LibraryCardRepository extends CrudRepository<LibraryCard, LibraryCard.Id> {
    @Override
    List<LibraryCard> findAll();

    List<LibraryCard> findByPerson_Id(Long id);
}
