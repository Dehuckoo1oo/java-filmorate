package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> update(Film film) {
        String sqlQuery = "UPDATE PUBLIC.films " +
                "SET name=?, description=?, releaseDate=?, duration=?" +
                "WHERE ID=?";
        String sqlQueryMPA = "INSERT INTO PUBLIC.MPA_films" +
                "(film_id, MPA_id)" +
                "VALUES(?, ?)";
        String sqlQueryGenre = "MERGE INTO PUBLIC.genre_film" +
                "(film_id, genre_id)" +
                "VALUES(?, ?)";
        String sqlQueryLikes = "MERGE INTO PUBLIC.likes_film_list" +
                "(film_id, user_like_id)" +
                "VALUES(?, ?)";
        String sqlQueryDelMPA = "DELETE FROM PUBLIC.MPA_films WHERE film_id=?";
        String sqlQueryDelGenre = "DELETE FROM PUBLIC.genre_film WHERE film_id=?";
        String sqlQueryDelLikes = "DELETE FROM PUBLIC.likes_film_list WHERE film_id=?";
        Long filmId = film.getId();
        MPA mpa = film.getMpa();
        List<Genre> genre = film.getGenres();
        Set<Long> likes = film.getLikes();
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getId());
        jdbcTemplate.update(sqlQueryDelMPA, filmId);
        jdbcTemplate.update(sqlQueryMPA, filmId, mpa.getId());
        jdbcTemplate.update(sqlQueryDelGenre, filmId);
        jdbcTemplate.update(sqlQueryDelLikes, filmId);
        if (genre != null) {
            for (Genre gnr : genre) {
                jdbcTemplate.update(sqlQueryGenre, filmId, gnr.getId());
            }
        }
        if (!likes.isEmpty()) {
            for (Long like : likes) {
                jdbcTemplate.update(sqlQueryLikes, filmId, like);
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public Optional<Film> delete(Film film) {
        String sqlQuery = "DELETE FROM PUBLIC.films WHERE id=?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return Optional.of(film);
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO PUBLIC.films (name, description, releaseDate, duration)" +
                "values (?, ?, ?, ?)";
        Long filmId;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolder);
        filmId = keyHolder.getKey().longValue();
        film.setId(filmId);
        MPA mpa = film.getMpa();
        List<Genre> genre = film.getGenres();
        String sqlQueryMPA = "INSERT INTO PUBLIC.MPA_films " +
                "(film_id, MPA_id)" +
                "VALUES(?, ?);";
        jdbcTemplate.update(sqlQueryMPA, filmId, mpa.getId());
        if (genre != null) {
            String sqlQueryGenre = "MERGE INTO PUBLIC.genre_film " +
                    "(film_id, genre_id)" +
                    "VALUES(?, ?);";
            for (Genre gnr : genre) {
                jdbcTemplate.update(sqlQueryGenre, filmId, gnr.getId());
            }
        }
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        String sql = "SELECT * FROM PUBLIC.Films where id =?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        Long filmId = null;
        String name;
        name = null;
        String description = null;
        LocalDate releaseDate = null;
        int duration = -1;
        if (rs.next()) {
            filmId = rs.getLong("id");
            name = rs.getString("name");
            description = rs.getString("description");
            releaseDate = rs.getDate("releaseDate").toLocalDate();
            duration = rs.getInt("duration");
        }
        Film film = new Film(filmId, name, description, releaseDate, duration, fillGenre(id), fillMPA(id));
        fillLikes(film);
        if (filmId != null) {
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> get() {
        String sql = "SELECT * FROM PUBLIC.Films";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        List<Film> films = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
            int duration = rs.getInt("duration");
            Film film = new Film(id, name, description, releaseDate, duration, fillGenre(id), fillMPA(id));
            fillLikes(film);
            films.add(film);
        }
        return films;
    }

    private void fillLikes(Film film) {
        String sql = "SELECT user_like_id  FROM likes_film_list " +
                "WHERE FILM_ID  =? ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (rs.next()) {
            film.like(rs.getLong("user_like_id"));
        }
    }

    private List<Genre> fillGenre(Long id) {
        String sql = "SELECT distinct gf.GENRE_ID, g.GENRE_NAME FROM genre_film gf " +
                "LEFT JOIN GENRE g ON gf.GENRE_ID = g.GENRE_ID " +
                "WHERE gf.FILM_ID = ? ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        List<Genre> genre = new ArrayList<>();
        while (rs.next()) {
            genre.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
        }
        return genre;
    }

    private MPA fillMPA(Long id) {
        String sql = "SELECT mf.MPA_ID, m.MPA_NAME  FROM MPA_FILMS mf " +
                "LEFT JOIN MPA m ON mf.MPA_ID =m.MPA_ID " +
                "WHERE mf.FILM_ID  =? ";
        return jdbcTemplate.query(sql, new MPAMapper(), id).stream().findFirst().orElse(null);
    }

    public Optional<Genre> getGenreById(int id) {
        String sql = "SELECT distinct GENRE_ID, GENRE_NAME FROM GENRE " +
                "WHERE GENRE_ID = ? ";
        return jdbcTemplate.query(sql, new GenreMapper(), id).stream().findFirst();
    }

    public Optional<MPA> getMPAById(int id) {
        String sql = "SELECT MPA_ID, MPA_NAME  FROM MPA " +
                "WHERE MPA_ID  =? ";
        return jdbcTemplate.query(sql, new MPAMapper(), id).stream().findFirst();
    }

    public List<Genre> getGenre() {
        String sql = "SELECT distinct GENRE_ID, GENRE_NAME FROM GENRE ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        List<Genre> genre = new ArrayList<>();
        while (rs.next()) {
            genre.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
        }
        return genre;
    }

    public List<MPA> getMPA() {
        String sql = "SELECT MPA_ID, MPA_NAME  FROM MPA ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        List<MPA> MPA = new ArrayList<>();
        while (rs.next()) {
            MPA.add(new MPA(rs.getInt("MPA_id"), rs.getString("MPA_name")));
        }
        return MPA;
    }

}
