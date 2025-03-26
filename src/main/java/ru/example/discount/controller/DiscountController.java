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

@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService fixedDiscountService;
    private final DiscountService variableDiscountService;

    @GetMapping("/fixed")
    public BigDecimal calculateFixedDiscount(@RequestParam("price") BigDecimal price,
                                             @RequestParam("product_category") long productCategoryId,
                                             @RequestParam("client_category") long clientCategoryId) {
        return fixedDiscountService.calculateDiscount(price, productCategoryId, clientCategoryId);
    }

    @GetMapping("/variable")
    public BigDecimal calculateVariableDiscount(@RequestParam("price") BigDecimal price,
                                                @RequestParam("product_category") long productCategoryId,
                                                @RequestParam("client_category") long clientCategoryId) {
        return variableDiscountService.calculateDiscount(price, productCategoryId, clientCategoryId);
    }

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
