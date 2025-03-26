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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
@Tag(name = "Discount controller API", description = "Контроллер управления типом скидки")
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
        log.info("GET /discount/fixed: price = {}, productCategoryId = {}, clientCategoryId = {}",
                price,
                productCategoryId,
                clientCategoryId
        );
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
        log.info("GET /discount/variable: price = {}, productCategoryId = {}, clientCategoryId = {}",
                price,
                productCategoryId,
                clientCategoryId
        );
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
