package ru.example.discount.service;

import ru.example.discount.validation.annotation.Price;

import java.math.BigDecimal;

public interface DiscountService {

    BigDecimal calculateDiscount(@Price BigDecimal price, long productCategoryId, long clientCategoryId);
}
