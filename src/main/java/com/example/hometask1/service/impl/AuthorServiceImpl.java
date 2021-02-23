package com.example.hometask1.service.impl;

import com.example.hometask1.aspects.annotation.CrudOperation;
import com.example.hometask1.dto.AuthorDto;
import com.example.hometask1.dto.mapper.AuthorMapper;
import com.example.hometask1.model.Author;
import com.example.hometask1.model.Book;
import com.example.hometask1.repository.AuthorCustomRepository;
import com.example.hometask1.repository.AuthorRepository;
import com.example.hometask1.repository.BookRepository;
import com.example.hometask1.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    private final AuthorCustomRepository authorCustomRepository;

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.DELETE)
    public boolean removeAuthorCascadeMethod(Long id) {
        try {
            Author author = authorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            boolean isHasPerson = author.getBookList().stream().anyMatch(book -> !book.getPersons().isEmpty());
            if (isHasPerson) {
                return false;
            } else {
                authorRepository.delete(author);
                return true;
            }
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.DELETE)
    public boolean removeAuthor(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<Book> books = author.getBookList();
        if (books.isEmpty()) {
            authorRepository.delete(author);
            return true;
        } else {
            return false;
        }
    }

    public List<AuthorDto> getAuthorList() {
        List<AuthorDto> result = new ArrayList<>();
        authorRepository.findAll().forEach(author -> result.add(authorMapper.createAuthorDto(author)));
        return result;
    }

    public AuthorDto getAuthorWithBooks(Long id) {
        return authorMapper.createAuthorWithBooksDto(authorRepository.findById(id).get());
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.INSERT)
    public AuthorDto addAuthor(Author author) {
        List<Book> books = new ArrayList<>();
        if (author.getBookList() != null) {
            for (Book book : author.getBookList()) {
                if (book.getId() != null) {
                    Book b = bookRepository.findById(book.getId()).get();
                    b.setAuthor(author);
                    books.add(b);
                } else {
                    book.setAuthor(author);
                    books.add(book);
                }
            }
        }

        author.setBookList(books);
        return authorMapper.createAuthorWithBooksDto(authorRepository.save(author));
    }

    public List<AuthorDto> searchAuthors(String firstName, String lastName, String patronymic, LocalDate from, LocalDate to) {
        List<AuthorDto> result = new ArrayList<>();
        authorCustomRepository.findAuthors(firstName, lastName, patronymic, from, to).forEach(author -> {
            result.add(authorMapper.createAuthorDto(author));
        });
        return result;
    }

}

