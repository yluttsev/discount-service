package ru.example.discount.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Discount controller API", description = "Контроллер управления типом скидки")
public class DiscountController {

    private final DiscountService fixedDiscountService;
    private final DiscountService variableDiscountService;

    @Operation(summary = "Рассчитать фиксированную скидку", description = "Фиксированная скидка")
    @GetMapping("/fixed")
    public BigDecimal calculateFixedDiscount(
            @RequestParam("price") @Parameter(description = "Сумма, для которой предоставляется скидка") BigDecimal price,
            @RequestParam("product_category") @Parameter(description = "Уникальный идентификатор продукта") long productCategoryId,
            @RequestParam("client_category") @Parameter(description = "Уникальный идентификатор категории клиента") long clientCategoryId) {
        return fixedDiscountService.calculateDiscount(price, productCategoryId, clientCategoryId);
    }

    @Operation(summary = "Рассчитать случайную скидку", description = "Случайная скидка")
    @GetMapping("/variable")
    public BigDecimal calculateVariableDiscount(
            @RequestParam("price") @Parameter(description = "Сумма, для которой предоставляется скидка") BigDecimal price,
            @RequestParam("product_category") @Parameter(description = "Уникальный идентификатор категории продукта") long productCategoryId,
            @RequestParam("client_category") @Parameter(description = "Уникальный идентификатор категории клиента") long clientCategoryId) {
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
