package com.example.hometask1.service.impl;

import com.example.hometask1.dto.BookDto;
import com.example.hometask1.dto.GenreDto;
import com.example.hometask1.dto.mapper.BookMapper;
import com.example.hometask1.model.Author;
import com.example.hometask1.model.Book;
import com.example.hometask1.model.Genre;
import com.example.hometask1.model.Person;
import com.example.hometask1.repository.AuthorRepository;
import com.example.hometask1.repository.BookCustomRepository;
import com.example.hometask1.repository.BookRepository;
import com.example.hometask1.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookCustomRepository bookCustomRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookMapper bookMapper;

    private Long id = 1L;
    private Book book;
    private BookDto bookDto;

    @BeforeEach
    private void init() {
        id = 1L;
        book = new Book();
        book.setId(1L);
        book.setName("Crime and Punishment");

        bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setName(book.getName());
    }


    @Test
    void getBookByGenre() {
        final String genreName = "novel";
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("novel");
        Set<Book> books = new HashSet<>(Arrays.asList(book));
        genre.setBooks(books);

        Mockito.when(genreRepository.findByName(genreName)).thenReturn(genre);
        Mockito.when(bookMapper.createBookDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.getBookByGenre(genreName);
        assertEquals(1, result.size());
        assertEquals(bookDto, result.get(0));
        Mockito.verify(genreRepository, Mockito.times(1)).findByName(genreName);
    }

    @Test
    void getBookByAuthor() {
        Author author = new Author();
        author.setFirstName("Fedor");
        author.setLastName("Dosoevsky");
        author.setPatronymic("Mikhailovich");
        author.setBookList(Arrays.asList(book));

        Mockito.when(authorRepository.findAll(Example.of(author))).thenReturn(Arrays.asList(author));
        Mockito.when(bookMapper.createBookDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.getBookByAuthor(author.getFirstName(), author.getLastName(), author.getPatronymic());
        Mockito.verify(authorRepository, Mockito.times(1)).findAll(Example.of(author));
        assertEquals(1, result.size());
        assertEquals(bookDto, result.get(0));
    }

    @Test
    void addBook() {
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.createBookDto(book)).thenReturn(bookDto);
        BookDto result = bookService.addBook(book);

        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
        assertEquals(bookDto, result);
    }

    @Test
    void updateGenre() {
        Genre genre = new Genre();
        genre.setName("novel");
        GenreDto genreDto = new GenreDto();
        genre.setName(genre.getName());

        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        bookDto.setGenres(new HashSet<>(Arrays.asList(genreDto)));
        book.setGenres(new HashSet<>(Arrays.asList(genre)));

        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.createBookDto(book)).thenReturn(bookDto);

        BookDto result = bookService.updateGenre(book);
        assertEquals(bookDto, result);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(id);
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
    }

    @Test
    void removeBook() {
        Mockito.when(bookRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        boolean result = bookService.removeBook(id);

        assertEquals(true, result);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void removeBookFalse() {
        Mockito.when(bookRepository.existsById(id)).thenReturn(false);
        boolean result = bookService.removeBook(id);
        assertEquals(false, result);

        Mockito.when(bookRepository.existsById(id)).thenReturn(true);
        book.setPersons(Arrays.asList(new Person()));
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        result = bookService.removeBook(id);
        assertEquals(false, result);
        Mockito.verify(bookRepository, Mockito.times(2)).existsById(id);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void searchBooks() {
        final String genreName = "novel";
        final Integer year = 2010;
        final String filter = "bigger";

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("novel");
        Set<Book> books = new HashSet<>(Arrays.asList(book));
        genre.setBooks(books);
        book.setGenres(new HashSet<>(Arrays.asList(genre)));
        book.setPublicationDate(LocalDateTime.now());
        List<Book> list = Arrays.asList(book);

        Mockito.when(bookCustomRepository.findBooks(genreName, year, filter)).thenReturn(list);
        Mockito.when(bookMapper.createBookDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.searchBooks(genreName, year, filter);

        Mockito.verify(bookCustomRepository, Mockito.times(1)).findBooks(genreName, year, filter);
        BookDto bookDtoResult = result.get(0);
        assertEquals(bookDto, bookDtoResult);
    }
}