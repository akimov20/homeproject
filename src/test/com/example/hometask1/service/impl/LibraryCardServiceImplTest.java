package com.example.hometask1.service.impl;

import com.example.hometask1.dto.DebtorDto;
import com.example.hometask1.model.Book;
import com.example.hometask1.model.LibraryCard;
import com.example.hometask1.model.Person;
import com.example.hometask1.repository.LibraryCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LibraryCardServiceImplTest {
    @Mock
    private LibraryCardRepository libraryCardRepository;

    @InjectMocks
    private LibraryCardServiceImpl libraryCardService;

    private Person person;
    private Book book;

    @BeforeEach
    private void init() {
        person = new Person();
        person.setId(1L);
        person.setFirstName("Vladmir");
        person.setLastName("Akimov");

        book = new Book();
        book.setId(2L);
        book.setName("Crime and Punishment");
    }

    @Test
    void getDebtors() {
        LibraryCard card1 = new LibraryCard();
        card1.setBook(book);
        card1.setPerson(person);
        card1.setExpectedReturnDate(ZonedDateTime.now().minusWeeks(1));

        LibraryCard card2 = new LibraryCard();
        card2.setBook(book);
        card2.setPerson(person);
        card2.setExpectedReturnDate(ZonedDateTime.now().plusWeeks(1));

        LibraryCard card3 = new LibraryCard();
        card3.setBook(book);
        card3.setPerson(person);
        card3.setExpectedReturnDate(ZonedDateTime.now().minusWeeks(1));

        List<LibraryCard> list = Arrays.asList(card1, card2, card3);
        Mockito.when(libraryCardRepository.findAll()).thenReturn(list);
        List<DebtorDto> results = libraryCardService.getDebtors();
        assertEquals(2, results.size());
        Mockito.verify(libraryCardRepository, Mockito.times(1)).findAll();
    }

    @Test
    void extendTerm() {
        LibraryCard card = new LibraryCard();
        LibraryCard.Id libraryId = new LibraryCard.Id();
        libraryId.setBookId(book.getId());
        libraryId.setPersonId(person.getId());
        card.setId(libraryId);
        card.setExpectedReturnDate(ZonedDateTime.now());

        Mockito.when(libraryCardRepository.findById(libraryId)).thenReturn(Optional.of(card));
        libraryCardService.extendTerm(libraryId.getPersonId(), libraryId.getBookId(), 7);

        Mockito.verify(libraryCardRepository, Mockito.times(1)).findById(libraryId);
        Mockito.verify(libraryCardRepository, Mockito.times(1)).save(card);
    }
}