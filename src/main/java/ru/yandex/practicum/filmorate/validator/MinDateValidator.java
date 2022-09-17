package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MinDateValidator implements ConstraintValidator<MinDate, String> {
    private static final LocalDate minReleaseDate = LocalDate.of(1985, 12, 28);
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (value != null && !value.isEmpty()) {
            LocalDate date = LocalDate.parse(value,formatter);
            return date.isAfter(minReleaseDate);
        }
        return false;
    }

}
