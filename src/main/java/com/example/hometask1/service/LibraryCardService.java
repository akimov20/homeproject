package com.example.hometask1.service;

import com.example.hometask1.dto.DebtorDto;

import java.util.List;

public interface LibraryCardService {
    List<DebtorDto> getDebtors();

    void extendTerm(Long personId, Long bookId, int days);
}
