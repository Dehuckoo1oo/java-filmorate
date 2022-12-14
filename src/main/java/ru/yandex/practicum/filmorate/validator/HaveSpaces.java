package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = MinDateValidator.class)
@Documented

public @interface HaveSpaces {
    String message() default "В данном поле нельзя использовать пробелы";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
