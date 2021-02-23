package com.example.hometask1.controller;

import com.example.hometask1.dto.BookDto;
import com.example.hometask1.jsonview.View;
import com.example.hometask1.model.Book;
import com.example.hometask1.service.BookService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/genre/{name}")
    @JsonView(value = {View.AuthorView.class})
    public List<BookDto> getBookListByGenre(@PathVariable(name = "name") String name) {
        return bookService.getBookByGenre(name);
    }

    @GetMapping("/author")
    @JsonView(value = {View.AuthorView.class})
    public List<BookDto> getBookListByAuthor(
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "patronymic", required = false) String patronymic) {
        return bookService.getBookByAuthor(firstName, lastName, patronymic);
    }

    @PostMapping("/add")
    @JsonView(value = {View.AuthorView.class})
    public BookDto addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/genre/update")
    @JsonView(value = {View.AuthorView.class})
    public BookDto updateGenre(@RequestBody Book book) {
        return bookService.updateGenre(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeBook(@PathVariable(name = "id") Long id) {
        boolean isRemoved = bookService.removeBook(id);
        return isRemoved ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Такого нет id");
    }

    @GetMapping("/search")
    @JsonView(value = {View.AuthorView.class})
    public List<BookDto> searchBooks(@RequestParam(name = "genreName", required = false) String genreName,
                                     @RequestParam(name = "year", required = false) Integer year,
                                     @RequestParam(name = "filter", required = false) String filter) {
        return bookService.searchBooks(genreName, year, filter);
    }
}
