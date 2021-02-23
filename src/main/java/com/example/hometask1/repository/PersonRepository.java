package com.example.hometask1.repository;

import com.example.hometask1.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
