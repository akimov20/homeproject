package com.example.hometask1.service.impl;

import com.example.hometask1.aspects.annotation.CrudOperation;
import com.example.hometask1.dto.DebtorDto;
import com.example.hometask1.model.LibraryCard;
import com.example.hometask1.repository.LibraryCardRepository;
import com.example.hometask1.service.LibraryCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryCardServiceImpl implements LibraryCardService {
    private final LibraryCardRepository libraryCardRepository;

    public List<DebtorDto> getDebtors() {
        List<DebtorDto> results = new ArrayList<>();
        libraryCardRepository.findAll().forEach(libraryCard -> {
            if (libraryCard.getReturnDate() == null && libraryCard.getExpectedReturnDate().isBefore(ZonedDateTime.now())) {
                int days = (int) ChronoUnit.DAYS.between(libraryCard.getExpectedReturnDate().toLocalDate(),
                        ZonedDateTime.now().toLocalDate());
                DebtorDto debtorDto = new DebtorDto(libraryCard.getPerson(), libraryCard.getBook(), days);
                results.add(debtorDto);
            }
        });
        return results;
    }

    @Transactional
    @CrudOperation(operation = CrudOperation.Operation.UPDATE)
    public void extendTerm(Long personId, Long bookId, int days) {
        LibraryCard.Id id = new LibraryCard.Id();
        id.setBookId(bookId);
        id.setPersonId(personId);
        Optional<LibraryCard> cardOptional = libraryCardRepository.findById(id);
        if (cardOptional.isPresent()) {
            LibraryCard card = cardOptional.get();
            ZonedDateTime current = card.getExpectedReturnDate();
            card.setExpectedReturnDate(current.plusDays(days));
            libraryCardRepository.save(card);
        }
    }
}
