--liquibase formatted sql

--changeset Владимир:insert data
INSERT INTO author(author_id, created_date, last_modified_date, version, first_name, last_name, middle_name)
VALUES (1,'2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'Lev', 'Tolstoy', 'Nikolaevich');

INSERT INTO author(author_id, created_date, last_modified_date, version, first_name, last_name, middle_name)
VALUES (2, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'Fedor', 'Dostoevsky', 'Mihailovich');

INSERT INTO book(book_id, created_date, last_modified_date, version, name, author_id)
VALUES (1, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'War and Peace', 1);

INSERT INTO book(book_id, created_date, last_modified_date, version, name, author_id)
VALUES (2, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'Idiot', 2);

INSERT INTO book(book_id, created_date, last_modified_date, version, name, author_id)
VALUES (3, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'Crime and Punishment', 2);

INSERT INTO dim_genre(genre_id, created_date, last_modified_date, version, genre_name)
VALUES (1, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'dram');

INSERT INTO dim_genre(genre_id, created_date, last_modified_date, version, genre_name)
VALUES (2, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'novel');

INSERT INTO book_genre_link(book_id, genre_id) VALUES (1, 1);
INSERT INTO book_genre_link(book_id, genre_id) VALUES (1, 2);
INSERT INTO book_genre_link(book_id, genre_id) VALUES (3, 1);
INSERT INTO book_genre_link(book_id, genre_id) VALUES (2, 1);

INSERT INTO person(person_id, created_date, last_modified_date, version, birth_date, first_name, last_name, middle_name)
VALUES (1, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, '1995-03-24', 'Vladimir', 'Akimov', 'Alexandrovich');


INSERT INTO library_card(book_book_id, person_person_id, created_date, last_modified_date, version, expected_return_date)
VALUES (3, 1, '2020-11-25 12:50:22.091', '2020-11-25 12:50:22.091', 0, '2020-12-02 12:50:22.08');

INSERT INTO library_card(book_book_id, person_person_id, created_date, last_modified_date, version, expected_return_date)
VALUES (2, 1, '2020-11-25 12:50:22.091', '2020-11-25 12:50:22.091', 0, '2020-12-02 12:50:22.08');





