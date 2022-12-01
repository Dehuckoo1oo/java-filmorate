package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.model.FilmMapper;
import ru.yandex.practicum.filmorate.model.GenreMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
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
        List<Long> likes = new ArrayList<>(film.getLikes());
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getId());
        jdbcTemplate.update(sqlQueryDelMPA, filmId);
        jdbcTemplate.update(sqlQueryMPA, filmId, mpa.getId());
        jdbcTemplate.update(sqlQueryDelGenre, filmId);
        jdbcTemplate.update(sqlQueryDelLikes, filmId);
        if (genre != null) {
            jdbcTemplate.batchUpdate(
                    sqlQueryGenre,
                    new BatchPreparedStatementSetter() {

                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, filmId);
                            ps.setInt(2, genre.get(i).getId());
                        }

                        public int getBatchSize() {
                            return genre.size();
                        }

                    });
        }
        if (!likes.isEmpty()) {
            jdbcTemplate.batchUpdate(
                    sqlQueryLikes,
                    new BatchPreparedStatementSetter() {

                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, filmId);
                            ps.setLong(2, likes.get(i));
                        }

                        public int getBatchSize() {
                            return likes.size();
                        }

                    });
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
            jdbcTemplate.batchUpdate(
                    sqlQueryGenre,
                    new BatchPreparedStatementSetter() {

                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, filmId);
                            ps.setInt(2, genre.get(i).getId());
                        }

                        public int getBatchSize() {
                            return genre.size();
                        }

                    });
        }
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        String sql = "SELECT * FROM PUBLIC.Films where id =?";
        Optional<Film> film = jdbcTemplate.query(sql, new FilmMapper(), id).stream().findFirst();
        film.ifPresent(objFilm -> {
            fillGenreByFilm(objFilm);
            fillMPAByFilm(objFilm);
            fillLikesByFilm(objFilm);
        });
        return film;
    }

    @Override
    public List<Film> get() {
        String sql = "SELECT * FROM PUBLIC.Films";
        List<Film> films = jdbcTemplate.query(sql, new FilmMapper());
        Map<Long, List<Genre>> genres = getGenresOfFilms();
        Map<Long, MPA> MPAs = getMPAOfFilms();
        Map<Long, Set<Long>> likes = getLikesOfFilms();
        if (!films.isEmpty()) {
            films.forEach(film -> {
                Long film_id = film.getId();
                film.setGenres(genres.getOrDefault(film_id, new ArrayList<>()));
                film.setMpa(MPAs.getOrDefault(film_id, null));
                film.setLikes(likes.getOrDefault(film_id, new HashSet<>()));
            });
        }

        return films;
    }

    private void fillLikesByFilm(Film film) {
        String sql = "SELECT user_like_id FROM likes_film_list " +
                "WHERE FILM_ID  =? ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (rs.next()) {
            film.like(rs.getLong("user_like_id"));
        }
    }

    private void fillGenreByFilm(Film film) {
        String sql = "SELECT distinct gf.GENRE_ID, g.GENRE_NAME FROM genre_film gf " +
                "LEFT JOIN GENRE g ON gf.GENRE_ID = g.GENRE_ID " +
                "WHERE gf.FILM_ID = ? ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, film.getId());
        List<Genre> genre = new ArrayList<>();
        while (rs.next()) {
            genre.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
        }
        film.setGenres(genre);
    }

    private void fillMPAByFilm(Film film) {
        String sql = "SELECT mf.MPA_ID, m.MPA_NAME FROM MPA_FILMS mf " +
                "LEFT JOIN MPA m ON mf.MPA_ID = m.MPA_ID " +
                "WHERE mf.FILM_ID = ? ";
        film.setMpa(jdbcTemplate.query(sql, new MPAMapper(), film.getId()).stream().findFirst().orElse(null));
    }

    public Optional<Genre> getGenreById(int id) {
        String sql = "SELECT distinct GENRE_ID, GENRE_NAME FROM GENRE " +
                "WHERE GENRE_ID = ? ";
        return jdbcTemplate.query(sql, new GenreMapper(), id).stream().findFirst();
    }

    public Optional<MPA> getMPAById(int id) {
        String sql = "SELECT MPA_ID, MPA_NAME FROM MPA " +
                "WHERE MPA_ID = ? ";
        return jdbcTemplate.query(sql, new MPAMapper(), id).stream().findFirst();
    }

    public List<Genre> getGenre() {
        String sql = "SELECT distinct GENRE_ID, GENRE_NAME FROM GENRE ";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    public List<MPA> getMPA() {
        String sql = "SELECT MPA_ID, MPA_NAME FROM MPA ";
        return jdbcTemplate.query(sql, new MPAMapper());
    }

    private Map<Long, List<Genre>> getGenresOfFilms() {
        String sql = "SELECT distinct gf.FILM_ID, gf.GENRE_ID, g.GENRE_NAME FROM genre_film gf " +
                "LEFT JOIN GENRE g ON gf.GENRE_ID = g.GENRE_ID ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        Map<Long, List<Genre>> filmGenre = new HashMap<>();
        while (rs.next()) {
            Long film_id = rs.getLong("FILM_ID");
            if (filmGenre.containsKey(film_id)) {
                filmGenre.get(film_id).add(new Genre(rs.getInt("genre_id"),
                        rs.getString("genre_name")));
            } else {
                filmGenre.put(film_id, List.of(new Genre(rs.getInt("genre_id"),
                        rs.getString("genre_name"))));
            }
        }
        return filmGenre;
    }

    private Map<Long, Set<Long>> getLikesOfFilms() {
        String sql = "SELECT FILM_ID, user_like_id FROM likes_film_list ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        Map<Long, Set<Long>> filmLikes = new HashMap<>();
        while (rs.next()) {
            Long film_id = rs.getLong("FILM_ID");
            if (filmLikes.containsKey(film_id)) {
                filmLikes.get(film_id).add(rs.getLong("user_like_id"));
            } else {
                filmLikes.put(rs.getLong("FILM_ID"), Set.of(rs.getLong("user_like_id")));
            }
        }
        return filmLikes;
    }

    private Map<Long, MPA> getMPAOfFilms() {
        String sql = "SELECT mf.FILM_ID, mf.MPA_ID, m.MPA_NAME FROM MPA_FILMS mf " +
                "LEFT JOIN MPA m ON mf.MPA_ID = m.MPA_ID ";
        Map<Long, MPA> MPAOfFilms = new HashMap<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        while (rs.next()) {
            MPAOfFilms.put(rs.getLong("FILM_ID"), new MPA(rs.getInt("MPA_id")
                    , rs.getString("MPA_name")));
        }
        return MPAOfFilms;
    }
}
