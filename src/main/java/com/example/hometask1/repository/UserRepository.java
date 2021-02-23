package com.example.hometask1.repository;

import com.example.hometask1.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);
}
