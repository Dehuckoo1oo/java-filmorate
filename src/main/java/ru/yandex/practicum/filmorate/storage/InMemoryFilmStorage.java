package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Long id = 1L;
    private Map<Long, Film> films = new HashMap<>();

    @Override
    public Optional<Film> update(Film film) {
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> delete(Film film) {
        return Optional.of(films.remove(film.getId()));
    }

    @Override
    public Film create(Film film) {
        film.setId(id);
        films.put(id, film);
        log.info("Add:" + film);
        id++;
        return film;
    }

    @Override
    public List<Film> get() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.ofNullable(films.get(id));
    }
}
