package ru.example.discount.service.impl;

import ru.example.discount.service.DiscountService;

import java.math.BigDecimal;

public class FixedDiscountService implements DiscountService {

    @Override
    public BigDecimal calculateDiscount(BigDecimal price, long productCategoryId, long clientCategoryId) {
        return null;  // TODO
    }
}
