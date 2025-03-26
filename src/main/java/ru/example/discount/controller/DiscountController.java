package ru.example.discount.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный расчет для фиксированной скидки",
                    content = @Content(schema = @Schema(implementation = BigDecimal.class, example = "450.50"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Неверное значение цены: отрицательное или null"
            ),
            @ApiResponse(
                    responseCode = "404", description = "Правила для вычисления новой цены по фиксированной скидке не найдены"
            ),
            @ApiResponse(
                    responseCode = "500", description = "Внутрення ошибка сервера"
            )
    })
    @GetMapping("/fixed")
    public BigDecimal calculateFixedDiscount(
            @RequestParam("price") @Parameter(description = "Сумма, для которой предоставляется скидка") BigDecimal price,
            @RequestParam("product_category") @Parameter(description = "Уникальный идентификатор продукта") long productCategoryId,
            @RequestParam("client_category") @Parameter(description = "Уникальный идентификатор категории клиента") long clientCategoryId) {
        return fixedDiscountService.calculateDiscount(price, productCategoryId, clientCategoryId);
    }

    @Operation(summary = "Рассчитать случайную скидку", description = "Случайная скидка")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный расчет для случайной скидки",
                    content = @Content(schema = @Schema(implementation = BigDecimal.class, example = "350.50"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Неверное значение цены: отрицательное или null"
            ),
            @ApiResponse(
                    responseCode = "404", description = "Правила для вычисления новой цены по случайной скидке не найдены"
            ),
            @ApiResponse(
                    responseCode = "500", description = "Внутрення ошибка сервера"
            )
    })
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
