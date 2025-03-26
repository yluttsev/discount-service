package ru.example.discount.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Объединение валидаторов {@link DecimalMin} и {@link Digits}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = {})
@DecimalMin(value = "0.0", inclusive = false, message = "Цена не должна быть отрицательной")
@Digits(integer = 9, fraction = 2, message = "Неверная цена")
public @interface Price {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
