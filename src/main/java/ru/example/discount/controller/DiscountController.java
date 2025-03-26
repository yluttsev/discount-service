package ru.example.discount.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.example.discount.service.DiscountService;

import java.math.BigDecimal;

/**
 * Контроллер для расчета скидок
 */
@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService fixedDiscountService;
    private final DiscountService variableDiscountService;

    /**
     * Расчет фиксированной скидки
     *
     * @param price             стоимость продукта
     * @param productCategoryId ID категории продукта
     * @param clientCategoryId  ID категории клиента
     * @return Стоимость с учетом скидок
     */
    @GetMapping("/fixed")
    public BigDecimal calculateFixedDiscount(@RequestParam("price") BigDecimal price,
                                             @RequestParam("product_category") long productCategoryId,
                                             @RequestParam("client_category") long clientCategoryId) {
        return fixedDiscountService.calculateDiscount(price, productCategoryId, clientCategoryId);
    }

    /**
     * Расчет переменной скидки
     *
     * @param price             стоимость продукта
     * @param productCategoryId ID категории продукта
     * @param clientCategoryId  ID категории клиента
     * @return Стоимость с учетом скидок
     */
    @GetMapping("/variable")
    public BigDecimal calculateVariableDiscount(@RequestParam("price") BigDecimal price,
                                                @RequestParam("product_category") long productCategoryId,
                                                @RequestParam("client_category") long clientCategoryId) {
        return variableDiscountService.calculateDiscount(price, productCategoryId, clientCategoryId);
    }

    /**
     * Обработчик исключения {@link ConstraintViolationException}
     *
     * @param e объект исключения
     * @return {@link ProblemDetail} с сообщением об ошибке
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail validationException(ConstraintViolationException e) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getConstraintViolations()
                        .stream()
                        .findFirst()
                        .map(ConstraintViolation::getMessage)
                        .orElse("Ошибка валидации")
        );
    }
}
