INSERT INTO PUBLIC.USERS
(EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES('denis@mail.ru', 'denis', 'denis', '2022-01-01'),
      ('Alex@mail.ru', 'Alex', 'Alex', '2022-01-01'),
      ('Arcadiy@mail.ru', 'Arcadiy', 'Arcadiy', '2022-01-01');

INSERT INTO PUBLIC.FILMS
(NAME, DESCRIPTION, RELEASEDATE, DURATION)
VALUES('Avatar', 'qweqwe', '2021-01-01', 135),
      ('Властелин колец', 'qweqwe', '2021-01-01', 133),
      ('Однажды в Голливуде', 'qweqwe', '2021-01-01', 134);

INSERT INTO PUBLIC.MPA_FILMS
(FILM_ID, MPA_ID)
VALUES(1, 1),(2, 1),(3, 1);

