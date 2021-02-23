package com.example.hometask1.service.impl;

import com.example.hometask1.aspects.annotation.CrudOperation;
import com.example.hometask1.dto.PersonDto;
import com.example.hometask1.dto.mapper.PersonMapper;
import com.example.hometask1.model.Book;
import com.example.hometask1.model.LibraryCard;
import com.example.hometask1.model.Person;
import com.example.hometask1.repository.BookRepository;
import com.example.hometask1.repository.LibraryCardRepository;
import com.example.hometask1.repository.PersonRepository;
import com.example.hometask1.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;
    private final LibraryCardRepository libraryCardRepository;
    private final PersonMapper personMapper;

    @CrudOperation(operation = CrudOperation.Operation.INSERT)
    public PersonDto addPerson(Person person) {
        return personMapper.createPersonDto(personRepository.save(person));
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.UPDATE)
    public PersonDto updatePerson(Person person) throws EntityNotFoundException {
        if (personRepository.existsById(person.getId())) {
            person.setVersion(personRepository.findById(person.getId()).get().getVersion());
            return personMapper.createPersonDto(personRepository.save(person));
        } else {
            throw new EntityNotFoundException("Person is not found");
        }
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.DELETE)
    public boolean removePerson(Long id) {
        if (personRepository.existsById(id)) {
            for (Book book : personRepository.findById(id).get().getBookList()) {
                book.getPersons().removeIf(person -> person.getId().equals(id));
            }
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.DELETE)
    public boolean removePersonByFullName(String fullName) {
        boolean isRemoved = false;
        Iterator<Person> iterator = personRepository.findAll().iterator();

        while (iterator.hasNext()) {
            Person p = iterator.next();
            if (p.getLastName().concat(" " + p.getFirstName()).concat(" " + p.getPatronymic()).equals(fullName)) {
                for (Book book : personRepository.findById(p.getId()).get().getBookList()) {
                    book.getPersons().removeIf(person -> person.getId().equals(p.getId()));
                }
                personRepository.deleteById(p.getId());
                isRemoved = true;
            }
        }
        return isRemoved;
    }

    @Transactional
    public PersonDto getBookList(Long id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            return personMapper.createPersonWithBooksDto(person);
        }
        return null;
    }

    public List<Person> getPersons() {
        List<Person> personList = new ArrayList<>();
        Iterator<Person> iterator = personRepository.findAll().iterator();
        while (iterator.hasNext())
            personList.add(iterator.next());
        return personList;
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.UPDATE)
    public PersonDto takeBook(Long id, Long bookId) throws EntityNotFoundException {
        Person person = null;
        if (personRepository.existsById(id) && bookRepository.existsById(bookId)) {
            List<LibraryCard> cards = libraryCardRepository.findByPerson_Id(id);
            boolean isExpired = cards
                    .stream()
                    .anyMatch(libraryCard -> (libraryCard.getReturnDate() == null
                            && libraryCard.getExpectedReturnDate().isBefore(ZonedDateTime.now())));

            if (!isExpired) {
                person = personRepository.findById(id).get();
                Book book = bookRepository.findById(bookId).get();
                person.addBook(book);
                book.addPerson(person);

                LibraryCard card = new LibraryCard();
                card.setBook(book);
                card.setPerson(person);
                card.setExpectedReturnDate(ZonedDateTime.now().plusDays(7));
                libraryCardRepository.save(card);
            } else {
                return null;
            }
            return personMapper.createPersonWithBooksDto(person);
        }
        throw new EntityNotFoundException("entity not found");
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.UPDATE)
    public void returnBook(Long id, Long bookId) throws EntityNotFoundException {
        if (personRepository.existsById(id) && bookRepository.existsById(bookId)) {
            LibraryCard.Id cardId = new LibraryCard.Id();
            cardId.setPersonId(id);
            cardId.setBookId(bookId);
            LibraryCard card = libraryCardRepository.findById(cardId).get();
            card.setReturnDate(ZonedDateTime.now());
            libraryCardRepository.save(card);
        } else {
            throw new EntityNotFoundException("Error");
        }
    }
}

