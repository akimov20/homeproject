package com.example.hometask1.service.impl;

import com.example.hometask1.dto.AuthorDto;
import com.example.hometask1.dto.BookDto;
import com.example.hometask1.dto.PersonDto;
import com.example.hometask1.dto.mapper.PersonMapper;
import com.example.hometask1.model.Book;
import com.example.hometask1.model.LibraryCard;
import com.example.hometask1.model.Person;
import com.example.hometask1.repository.BookRepository;
import com.example.hometask1.repository.LibraryCardRepository;
import com.example.hometask1.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LibraryCardRepository libraryCardRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person;
    private PersonDto personDto;
    private Long id = 1L;

    @BeforeEach
    private void init() {
        id = 1L;
        person = new Person();
        person.setId(id);
        person.setFirstName("Vladimir");
        person.setLastName("Akimov");
        person.setPatronymic("Alexandrovich");
        person.setBirthday(new Date());

        personDto = new PersonDto();
        personDto.setBirthday(person.getBirthday());
        personDto.setFirstName(person.getFirstName());
        personDto.setId(person.getId());
        personDto.setLastName(person.getLastName());
        personDto.setPatronymic(person.getPatronymic());
    }

    @Test
    void addPerson() {
        Mockito.when(personRepository.save(person)).thenReturn(person);
        Mockito.when(personMapper.createPersonDto(person)).thenReturn(personDto);
        PersonDto result = personService.addPerson(person);

        Mockito.verify(personRepository, Mockito.times(1)).save(person);
        assertEquals(personDto, result);
    }

    @Test
    void updatePerson() {
        Mockito.when(personRepository.existsById(id)).thenReturn(true);
        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));
        Mockito.when(personRepository.save(person)).thenReturn(person);
        Mockito.when(personMapper.createPersonDto(person)).thenReturn(personDto);
        PersonDto result = personService.updatePerson(person);

        Mockito.verify(personRepository, Mockito.times(1)).save(person);
        Mockito.verify(personRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(personRepository, Mockito.times(1)).findById(id);
        assertEquals(personDto, result);
    }

    @Test
    void removePerson() {
        Mockito.when(personRepository.existsById(id)).thenReturn(true);
        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));
        boolean result = personService.removePerson(id);
        assertEquals(true, result);
        Mockito.verify(personRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(personRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void removePersonFalse() {
        Mockito.when(personRepository.existsById(id)).thenReturn(false);
        boolean result = personService.removePerson(id);
        assertEquals(false, result);
        Mockito.verify(personRepository, Mockito.times(1)).existsById(id);
    }

    @Test
    void removePersonByFullName() {
        final String fullName = "Akimov Vladimir Alexandrovich";
        Mockito.when(personRepository.findAll()).thenReturn(Arrays.asList(person));
        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));
        boolean result = personService.removePersonByFullName(fullName);
        assertEquals(true, result);
        Mockito.verify(personRepository, Mockito.times(1)).findAll();
        Mockito.verify(personRepository, Mockito.times(1)).findById(id);
        Mockito.verify(personRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void removePersonByFullNameFalse() {
        final String uncorrectedFullName = "Uncorrected Name";
        Mockito.when(personRepository.findAll()).thenReturn(Arrays.asList(person));
        boolean result = personService.removePersonByFullName(uncorrectedFullName);
        assertEquals(false, result);
        Mockito.verify(personRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getBookList() {
        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));
        personDto.setBookList(Arrays.asList(new BookDto()));
        Mockito.when(personMapper.createPersonWithBooksDto(person)).thenReturn(personDto);
        PersonDto result = personService.getBookList(id);

        assertEquals(personDto, result);
        Mockito.verify(personRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void getPersons() {
        Mockito.when(personRepository.findAll()).thenReturn(Arrays.asList(person));
        List<Person> result = personService.getPersons();

        Mockito.verify(personRepository, Mockito.times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(person), result);
    }

    @Test
    void takeBook() {
        Book newBook = new Book();
        newBook.setId(1L);
        newBook.setName("Idiot");
        Book takenBook = new Book();
        takenBook.setId(2L);
        takenBook.setName("War and Peace");
        LibraryCard card = new LibraryCard();
        card.setPerson(person);
        card.setBook(takenBook);
        card.setReturnDate(null);
        card.setExpectedReturnDate(ZonedDateTime.now().plusWeeks(7));

        Mockito.when(personRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.existsById(newBook.getId())).thenReturn(true);
        Mockito.when(libraryCardRepository.findByPerson_Id(person.getId())).thenReturn(Arrays.asList(card));
        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(newBook));

        AuthorDto authorDto = new AuthorDto();
        authorDto.setFirstName("Fedor");
        authorDto.setLastName("Dostoevsky");
        BookDto bookDto1 = new BookDto();
        bookDto1.setId(takenBook.getId());
        bookDto1.setName(takenBook.getName());
        bookDto1.setAuthor(authorDto);
        BookDto bookDto2 = new BookDto();
        bookDto2.setId(newBook.getId());
        bookDto2.setName(newBook.getName());
        bookDto2.setAuthor(authorDto);

        personDto.setBookList(Arrays.asList(bookDto1, bookDto2));
        Mockito.when(personMapper.createPersonWithBooksDto(person)).thenReturn(personDto);


        PersonDto result = personService.takeBook(id, newBook.getId());
        assertEquals(personDto, result);
        Mockito.verify(personRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(newBook.getId());
        Mockito.verify(libraryCardRepository, Mockito.times(1)).findByPerson_Id(person.getId());
        Mockito.verify(personRepository, Mockito.times(1)).findById(id);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void takeBookFalse() {
        Book newBook = new Book();
        newBook.setId(1L);
        newBook.setName("Idiot");
        Book takenBook = new Book();
        takenBook.setId(2L);
        takenBook.setName("War and Peace");
        LibraryCard card = new LibraryCard();
        card.setPerson(person);
        card.setBook(takenBook);
        card.setReturnDate(null);
        card.setExpectedReturnDate(ZonedDateTime.now().minusWeeks(7));

        Mockito.when(personRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.existsById(newBook.getId())).thenReturn(true);
        Mockito.when(libraryCardRepository.findByPerson_Id(person.getId())).thenReturn(Arrays.asList(card));


        BookDto bookDto1 = new BookDto();
        bookDto1.setId(takenBook.getId());
        bookDto1.setName(takenBook.getName());
        BookDto bookDto2 = new BookDto();
        bookDto2.setId(newBook.getId());
        bookDto2.setName(newBook.getName());

        PersonDto result = personService.takeBook(id, newBook.getId());
        assertEquals(null, result);
        Mockito.verify(personRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(newBook.getId());
        Mockito.verify(libraryCardRepository, Mockito.times(1)).findByPerson_Id(person.getId());
    }

    @Test
    void takeBookFalseException() {
        Book newBook = new Book();
        newBook.setId(1L);
        newBook.setName("Idiot");
        LibraryCard card = new LibraryCard();
        card.setPerson(person);
        card.setReturnDate(null);
        card.setExpectedReturnDate(ZonedDateTime.now().plusWeeks(7));

        Mockito.when(personRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.existsById(newBook.getId())).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            personService.takeBook(id, newBook.getId());
        });
    }

    @Test
    void returnBook() {
        Book takenBook = new Book();
        takenBook.setId(2L);
        takenBook.setName("War and Peace");
        LibraryCard card = new LibraryCard();
        card.setPerson(person);
        card.setBook(takenBook);

        Mockito.when(personRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.existsById(takenBook.getId())).thenReturn(true);
        Mockito.when(libraryCardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        personService.returnBook(id, takenBook.getId());

        Mockito.verify(personRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(takenBook.getId());
        Mockito.verify(libraryCardRepository, Mockito.times(1)).findById(card.getId());
        Mockito.verify(libraryCardRepository, Mockito.times(1)).save(card);
    }

    @Test
    void returnBookFalseException() {
        Book takenBook = new Book();
        takenBook.setId(2L);
        takenBook.setName("War and Peace");
        LibraryCard card = new LibraryCard();
        card.setPerson(person);
        card.setBook(takenBook);

        Mockito.when(personRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.existsById(takenBook.getId())).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            personService.takeBook(id, takenBook.getId());
        });
    }
}