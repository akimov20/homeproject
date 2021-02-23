delete from book_genre_link;
delete from library_card;
delete from book;
delete from author;
delete from dim_genre;
delete from person;


alter sequence author_author_id_seq restart with 20;
alter sequence book_book_id_seq restart with 20;
alter sequence dim_genre_genre_id_seq restart with 20;
alter sequence library_user_user_id_seq restart with 20;
alter sequence person_person_id_seq restart with 20;
