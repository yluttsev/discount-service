package ru.example.discount.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum DiscountType {
    @Schema(description = "Фиксированная скидка")
    FIXED,
    @Schema(description = "Случайная скидка")
    VARIABLE
}
