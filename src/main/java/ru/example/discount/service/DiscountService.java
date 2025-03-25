package ru.example.discount.service;

import java.math.BigDecimal;

public interface DiscountService {

    BigDecimal calculateDiscount(BigDecimal price, long productCategoryId, long clientCategoryId);
}
