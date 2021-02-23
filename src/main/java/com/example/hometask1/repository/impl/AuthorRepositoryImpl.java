package com.example.hometask1.repository.impl;

import com.example.hometask1.model.Author;
import com.example.hometask1.repository.AuthorCustomRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Author> findAuthors(String firstName, String lastName, String patronymic, LocalDate from, LocalDate to) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> author = query.from(Author.class);
        List<Predicate> conditions = new ArrayList<>();
        final String createdDate = "createdDate";

        if (firstName != null) {
            Predicate predicate = cb.equal(author.get("firstName"), firstName);
            conditions.add(predicate);
        }
        if (lastName != null) {
            Predicate predicate = cb.equal(author.get("lastName"), lastName);
            conditions.add(predicate);
        }
        if (patronymic != null) {
            Predicate predicate = cb.equal(author.get("patronymic"), patronymic);
            conditions.add(predicate);
        }

        if (from != null && to != null) {
            Predicate predicate = cb.between(author.get(createdDate), from.atStartOfDay(ZoneOffset.UTC), to.atStartOfDay(ZoneOffset.UTC));
            conditions.add(predicate);
        } else {
            if (from != null) {
                Predicate predicate = cb.greaterThanOrEqualTo(author.get(createdDate), from.atStartOfDay(ZoneOffset.UTC));
                conditions.add(predicate);
            }
            if (to != null) {
                Predicate predicate = cb.lessThanOrEqualTo(author.get(createdDate), to.atStartOfDay(ZoneOffset.UTC));
                conditions.add(predicate);
            }
        }

        query.where(conditions.toArray(new Predicate[]{}));
        TypedQuery<Author> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
