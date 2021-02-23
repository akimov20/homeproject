package com.example.hometask1.repository.impl;

import com.example.hometask1.model.Book;
import com.example.hometask1.repository.BookCustomRepository;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> findBooks(String genreName, Integer year, String filter) throws IllegalArgumentException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        List<Predicate> conditions = new ArrayList<>();
        final String publicationDate = "publicationDate";
        final String date = "year";

        if (year != null) {
            if (filter == null) {
                throw new IllegalArgumentException("Uncorrected argument");
            } else {
                switch (filter) {
                    case "bigger":
                        conditions.add(cb.greaterThan(cb.function(date, Integer.class, root.get(publicationDate)), year));
                        break;

                    case "less":
                        conditions.add(cb.lessThan(cb.function(date, Integer.class, root.get(publicationDate)), year));
                        break;

                    case "equals":
                        conditions.add(cb.equal(cb.function(date, Integer.class, root.get(publicationDate)), year));
                        break;

                    default:
                        throw new IllegalArgumentException("Uncorrected argument");
                }

            }
        }

        if (genreName != null && !genreName.isEmpty()) {
            Join<Object, Object> genre = (Join<Object, Object>) root.fetch("genres");
            conditions.add(cb.equal(genre.get("name"), genreName));
        }

        cq.where(conditions.toArray(new Predicate[]{}));
        return entityManager.createQuery(cq).getResultList();
    }
}
