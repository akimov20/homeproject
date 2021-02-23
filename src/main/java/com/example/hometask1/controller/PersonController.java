package com.example.hometask1.controller;

import com.example.hometask1.dto.DebtorDto;
import com.example.hometask1.dto.PersonDto;
import com.example.hometask1.jsonview.View;
import com.example.hometask1.model.Person;
import com.example.hometask1.service.LibraryCardService;
import com.example.hometask1.service.PersonService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final LibraryCardService libraryCardService;

    @GetMapping("/{id}/books")
    @JsonView(value = View.AuthorView.class)
    public PersonDto getBookListByPerson(@PathVariable(name = "id") Long id) {
        return personService.getBookList(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removePerson(@PathVariable(name = "id") Long id) {
        boolean isRemoved = personService.removePerson(id);
        return isRemoved ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Такого нет id");
    }

    @DeleteMapping("/byName/{fullName}")
    public ResponseEntity removePersonByFullName(@PathVariable(name = "fullName") String fullName) {
        boolean isRemoved = personService.removePersonByFullName(fullName);
        return isRemoved ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Такого нет id");
    }

    @PutMapping("/{id}/books/add/{bookId}")
    @JsonView(value = View.AuthorView.class)
    public ResponseEntity takeBook(@PathVariable(name = "id") Long id, @PathVariable(name = "bookId") Long bookId) {
        try {
            PersonDto result = personService.takeBook(id, bookId);
            return result == null ? ResponseEntity
                    .badRequest().body("Нельзя получить новую книгу, пока не вернёшь старую!")
                    : new ResponseEntity<>(result, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/books/remove/{bookId}")
    @JsonView(value = View.AuthorView.class)
    public PersonDto removeBook(@PathVariable(name = "id") Long id, @PathVariable(name = "bookId") Long bookId) {
        personService.returnBook(id, bookId);
        return personService.getBookList(id);
    }

    @PostMapping("/add")
    @JsonView(value = View.MainInfoView.class)
    public PersonDto addPerson(@RequestBody Person person) {
        return personService.addPerson(person);
    }

    @PostMapping("/update")
    @JsonView(value = View.MainInfoView.class)
    public ResponseEntity updatePerson(@RequestBody Person person) {
        try {
            PersonDto result = personService.updatePerson(person);
            return ResponseEntity.ok().body(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/extern/{personId}/{bookId}/{days}")
    public ResponseEntity externBook(@PathVariable(name = "personId") Long personId,
                                     @PathVariable(name = "bookId") Long bookId,
                                     @PathVariable(name = "days") int days) {
        libraryCardService.extendTerm(personId, bookId, days);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/debtors")
    public List<DebtorDto> getDebtors() {
        return libraryCardService.getDebtors();
    }
}
