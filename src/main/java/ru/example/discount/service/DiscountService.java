package ru.example.discount.service;

import java.math.BigDecimal;

public interface DiscountService {
    // Вычисление скидки
    BigDecimal calculateDiscount(BigDecimal price);
}
