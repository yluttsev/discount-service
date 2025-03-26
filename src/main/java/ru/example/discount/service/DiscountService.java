package ru.example.discount.service;

import ru.example.discount.validation.annotation.Price;

import java.math.BigDecimal;

/**
 * Базовый интерфейс сервиса для расчета скидок
 */
public interface DiscountService {

    /**
     * Расчет скидки
     *
     * @param price             стоимость продукта
     * @param productCategoryId ID категории продукта
     * @param clientCategoryId  ID категории клиента
     * @return Стоимость с учетом всех скидок
     */
    BigDecimal calculateDiscount(@Price BigDecimal price, long productCategoryId, long clientCategoryId);
}
