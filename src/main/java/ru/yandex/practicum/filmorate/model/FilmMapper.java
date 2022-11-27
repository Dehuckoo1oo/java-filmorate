package ru.yandex.practicum.filmorate.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getLong("id"), rs.getString("name"),
                rs.getString("description"), rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"), new ArrayList<>(), new MPA(-1, null));
    }
}
