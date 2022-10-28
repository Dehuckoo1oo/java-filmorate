
https://dbdiagram.io/d/635b89435170fb6441adf755

CREATE TABLE "users" (
  "id" SERIAL PRIMARY KEY,
  "email" varchar NOT NULL,
  "login" varchar NOT NULL,
  "name" varchar,
  "birthday" date
);

CREATE TABLE "friend_list" (
  "user_id" long NOT NULL,
  "friend_id" long,
  "isAcceptRequest" boolean,
  PRIMARY KEY ("user_id", "friend_id")
);

CREATE TABLE "films" (
  "id" long PRIMARY KEY,
  "name" varchar,
  "description" varchar,
  "releaseDate" date,
  "duration" int
);

CREATE TABLE "MPA_films" (
  "film_id" long NOT NULL,
  "MPA_id" int,
  PRIMARY KEY ("film_id", "MPA_id")
);

CREATE TABLE "MPA" (
  "MPA_id" int PRIMARY KEY,
  "MPA_name" varchar
);

CREATE TABLE "genre_film" (
  "film_id" long,
  "genre_id" int,
  PRIMARY KEY ("film_id", "genre_id")
);

CREATE TABLE "genre" (
  "genre_id" int PRIMARY KEY,
  "genre_name" varchar
);

CREATE TABLE "likes_film_list" (
  "film_id" long,
  "user_like_id" long,
  PRIMARY KEY ("film_id", "user_like_id")
);

ALTER TABLE "friend_list" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "friend_list" ADD FOREIGN KEY ("friend_id") REFERENCES "users" ("id");

ALTER TABLE "MPA_films" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "MPA_films" ADD FOREIGN KEY ("MPA_id") REFERENCES "MPA" ("MPA_id");

ALTER TABLE "genre_film" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "genre_film" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id");

ALTER TABLE "likes_film_list" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "likes_film_list" ADD FOREIGN KEY ("user_like_id") REFERENCES "users" ("id");
