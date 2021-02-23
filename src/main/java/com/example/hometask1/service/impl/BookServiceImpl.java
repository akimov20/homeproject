package com.example.hometask1.service.impl;

import com.example.hometask1.aspects.annotation.CrudOperation;
import com.example.hometask1.dto.BookDto;
import com.example.hometask1.dto.mapper.BookMapper;
import com.example.hometask1.model.Author;
import com.example.hometask1.model.Book;
import com.example.hometask1.model.Genre;
import com.example.hometask1.repository.AuthorRepository;
import com.example.hometask1.repository.BookCustomRepository;
import com.example.hometask1.repository.BookRepository;
import com.example.hometask1.repository.GenreRepository;
import com.example.hometask1.service.BookService;
import liquibase.pro.packaged.D;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;
    private final BookCustomRepository bookCustomRepository;

    public List<BookDto> getBookByGenre(String name) {
        List<BookDto> result = new ArrayList<>();
        genreRepository
                .findByName(name)
                .getBooks()
                .forEach(book -> result.add(bookMapper.createBookDto(book)));
        return result;
    }

    public List<BookDto> getBookByAuthor(String firstName, String lastName, String patronymic) {
        List<BookDto> result = new ArrayList<>();
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setPatronymic(patronymic);

        Example<Author> example = Example.of(author);
        List<Author> authors = authorRepository.findAll(example);
        authors.forEach(a -> a.getBookList()
                .forEach(book -> result.add(bookMapper.createBookDto(book))));
        return result;
    }


    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.INSERT)
    public BookDto addBook(Book book) {
        if (book.getAuthor() != null && book.getAuthor().getId() != null) {
            book.setAuthor(authorRepository.findById(book.getAuthor().getId()).get());
        }

        Set<Genre> genres = new HashSet<>();
        if (book.getGenres() != null) {
            for (Genre genre : book.getGenres()) {
                if (genre.getId() != null) {
                    genres.add(genreRepository.findById(genre.getId()).get());
                } else {
                    genres.add(genre);
                }
            }
        }

        book.setGenres(genres);
        return bookMapper.createBookDto(bookRepository.save(book));
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.UPDATE)
    public BookDto updateGenre(Book book) {
        Book b = bookRepository.findById(book.getId()).get();
        Set<Genre> genres = book.getGenres();
        Set<Genre> genreSet = b.getGenres();
        Set<Long> ids = new HashSet<>();


        for (Genre g : genreSet) {
            if (!genres.contains(g)) {
                Long id = g.getId();
                ids.add(id);
            }
        }

        for (Long id : ids)
            b.getGenres().removeIf(s -> s.getId().equals(id));

        Set<Genre> data = new HashSet<>();
        for (Genre g : genres) {
            if (!b.getGenres().contains(g)) {
                if (g.getId() != null) {
                    g = genreRepository.findById(g.getId()).get();
                    g.getBooks().add(b);
                    data.add(g);
                } else {
                    g.getBooks().add(b);
                    data.add(g);
                }
            }
        }

        for (Genre g : data) {
            b.getGenres().add(g);
        }
        return bookMapper.createBookDto(bookRepository.save(b));
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.DELETE)
    public boolean removeBook(Long id) {
        if (bookRepository.existsById(id)) {
            if (!bookRepository.findById(id).get().getPersons().isEmpty()) {
                return false;
            } else {
                bookRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    public List<BookDto> searchBooks(String genreName, Integer year, String filter) {
        List<BookDto> result = new ArrayList<>();
        bookCustomRepository.findBooks(genreName, year, filter).forEach(book -> {
            result.add(bookMapper.createBookDto(book));
        });
        return result;
    }

}