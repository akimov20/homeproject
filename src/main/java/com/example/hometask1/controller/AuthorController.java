package com.example.hometask1.controller;

import com.example.hometask1.dto.AuthorDto;
import com.example.hometask1.jsonview.View;
import com.example.hometask1.model.Author;
import com.example.hometask1.service.AuthorService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    @JsonView(value = {View.AuthorView.class})
    public List<AuthorDto> getAuthorList() {
        return authorService.getAuthorList();
    }

    @GetMapping("/{id}")
    @JsonView(value = {View.BookView.class})
    public AuthorDto getAuthorsWithBooks(@PathVariable(name = "id") Long id) {
        return authorService.getAuthorWithBooks(id);
    }

    @DeleteMapping("/{id}/cascade")
    public ResponseEntity removeAuthorCascadeMethod(@PathVariable(name = "id") Long id) {
        boolean isRemoved = authorService.removeAuthorCascadeMethod(id);
        return isRemoved ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Такого нет id");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeAuthor(@PathVariable(name = "id") Long id) {
        try {
            boolean isRemoved = authorService.removeAuthor(id);
            if (isRemoved) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.ok().body("Author is not removed. Author has books");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Author is not found");
        }
    }

    @PostMapping("/add")
    @JsonView(value = {View.BookView.class})
    public AuthorDto addAuthor(@RequestBody Author author) {
        return authorService.addAuthor(author);
    }

    @GetMapping("/search")
    @JsonView(value = {View.AuthorView.class})
    public List<AuthorDto> searchAuthors(@RequestParam(name = "firstName", required = false) String firstName,
                                         @RequestParam(name = "lastName", required = false) String lastName,
                                         @RequestParam(name = "patronymic", required = false) String patronymic,
                                         @RequestParam(name = "from", required = false) LocalDate from,
                                         @RequestParam(name = "to", required = false) LocalDate to) {
        return authorService.searchAuthors(firstName, lastName, patronymic, from, to);
    }
}

