
insert into author(author_id, created_date, last_modified_date, version, first_name, last_name, middle_name)
values (1,'1995-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'Lev', 'Tolstoy', 'Nikolaevich');

insert into author(author_id, created_date, last_modified_date, version, first_name, last_name, middle_name)
values (2, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'Fedor', 'Dostoevsky', 'Mihailovich');

insert into book(book_id, created_date, last_modified_date, version, name, author_id, publication_date)
values (1, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'War and Peace', 1, '1880-11-25');

insert into book(book_id, created_date, last_modified_date, version, name, author_id, publication_date)
values (2, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'Idiot', 2, '1888-11-25');

insert into book(book_id, created_date, last_modified_date, version, name, author_id, publication_date)
values (3, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'Crime and Punishment', 2, '2054-11-25');

insert into dim_genre(genre_id, created_date, last_modified_date, version, genre_name)
values (1, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'dram');

insert into dim_genre(genre_id, created_date, last_modified_date, version, genre_name)
values (2, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, 'novel');

insert into book_genre_link(book_id, genre_id) values (1, 1);
insert into book_genre_link(book_id, genre_id) values (1, 2);
insert into book_genre_link(book_id, genre_id) values (3, 1);
insert into book_genre_link(book_id, genre_id) values (2, 1);


insert into person(person_id, created_date, last_modified_date, version, birth_date, first_name, last_name, middle_name)
values (1, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, '1995-3-24', 'Vladimir', 'Akimov', 'Alexandrovich');

insert into person(person_id, created_date, last_modified_date, version, birth_date, first_name, last_name, middle_name)
values (2, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, '1995-3-24', 'Sergey', 'Dmitryev', 'Romanovich');

insert into person(person_id, created_date, last_modified_date, version, birth_date, first_name, last_name, middle_name)
values (3, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, '1995-3-24', 'Andrey', 'Romanov', 'Konstantinovich');

insert into person(person_id, created_date, last_modified_date, version, birth_date, first_name, last_name, middle_name)
values (4, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839', 0, '1995-3-24', 'Andrey', 'Akimov', 'Alexandrovich');

insert into library_card(book_book_id, person_person_id, created_date, last_modified_date,
version, expected_return_date, return_date) values (1, 2, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839',
0, '2020-11-25', '2020-10-25');

insert into library_card(book_book_id, person_person_id, created_date, last_modified_date,
version, expected_return_date, return_date) values (1, 1, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839',
0, '5000-11-25', null);

insert into library_card(book_book_id, person_person_id, created_date, last_modified_date,
version, expected_return_date, return_date) values (1, 3, '2020-11-25 12:50:21.839', '2020-11-25 12:50:21.839',
0, '2010-11-25', null);


