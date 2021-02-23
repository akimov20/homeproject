package com.example.hometask1.service.impl;

import com.example.hometask1.dto.AuthorDto;
import com.example.hometask1.dto.BookDto;
import com.example.hometask1.dto.mapper.AuthorMapper;
import com.example.hometask1.model.Author;
import com.example.hometask1.model.Book;
import com.example.hometask1.repository.AuthorCustomRepository;
import com.example.hometask1.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorCustomRepository authorCustomRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorDto authorDto;
    private List<Book> books;
    private Long id = 1L;

    @BeforeEach
    private void init() {
        author = new Author();
        author.setId(id);
        author.setFirstName("Fedor");
        author.setLastName("Dostoevsky");
        author.setPatronymic("Mickailovich");

        Book book = new Book();
        book.setId(id);
        book.setName("Crime and Punishment");
        book.setAuthor(author);

        books = new ArrayList<>();
        books.add(book);
        author.setBookList(books);

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setName(book.getName());

        authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setFirstName("Fedor");
        authorDto.setLastName("Dostoevsky");
        authorDto.setPatronymic("Mickailovich");
        authorDto.setBookList(Arrays.asList(bookDto));
        bookDto.setAuthor(authorDto);
    }


    @Test
    void removeAuthorCascadeMethod() {
        Mockito.when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        boolean result = authorService.removeAuthorCascadeMethod(id);
        Mockito.verify(authorRepository, Mockito.times(1)).delete(author);
        assertEquals(true, result);
    }

    @Test
    void removeAuthor() {
        author.getBookList().clear();
        Mockito.when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        boolean result = authorService.removeAuthor(id);
        Mockito.verify(authorRepository, Mockito.times(1)).delete(author);
        assertEquals(true, result);
    }

    @Test
    void getAuthorList() {
        List<Author> list = new ArrayList<>();
        list.add(author);
        Mockito.when(authorRepository.findAll()).thenReturn(list);
        Mockito.when(authorMapper.createAuthorDto(author)).thenReturn(authorDto);

        List<AuthorDto> results = authorService.getAuthorList();
        assertEquals(1, results.size());
        Mockito.verify(authorRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getAuthorWithBooks() {
        Mockito.when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        Mockito.when(authorMapper.createAuthorWithBooksDto(author)).thenReturn(authorDto);

        AuthorDto res = authorService.getAuthorWithBooks(id);
        Mockito.verify(authorRepository, Mockito.times(1)).findById(id);

        Book b = books.get(0);
        assertEquals(b.getName(), res.getBookList().get(0).getName());
        assertEquals(b.getId(), res.getBookList().get(0).getId());
        assertEquals(b.getAuthor().getId(), res.getBookList().get(0).getAuthor().getId());
    }

    @Test
    void addAuthor() {
        Mockito.when(authorRepository.save(author)).thenReturn(author);
        Mockito.when(authorMapper.createAuthorWithBooksDto(author)).thenReturn(authorDto);
        author.setBookList(null);
        AuthorDto result = authorService.addAuthor(author);

        Mockito.verify(authorRepository, Mockito.times(1)).save(author);
        assertEquals(authorDto, result);
    }

    @Test
    void searchAuthors() {
        List<Author> list = Arrays.asList(author);
        Mockito.when(authorCustomRepository.findAuthors(author.getFirstName(), author.getLastName(),
                author.getPatronymic(), LocalDate.now(), LocalDate.now()))
                .thenReturn(list);
        Mockito.when(authorMapper.createAuthorDto(author)).thenReturn(authorDto);

        List<AuthorDto> result = authorService.searchAuthors(author.getFirstName(), author.getLastName(),
                author.getPatronymic(), LocalDate.now(), LocalDate.now());

        Mockito.verify(authorCustomRepository, Mockito.times(1)).findAuthors(author.getFirstName(),
                author.getLastName(), author.getPatronymic(), LocalDate.now(), LocalDate.now());

        AuthorDto authorDtoResult = result.get(0);
        assertEquals(authorDto, authorDtoResult);
    }
}