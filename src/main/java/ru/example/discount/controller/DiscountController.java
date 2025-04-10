package ru.example.discount.controller;

import feign.FeignException;
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
import ru.example.discount.service.impl.FixedDiscountService;
import ru.example.discount.service.impl.LoyaltyDiscountService;
import ru.example.discount.service.impl.VariableDiscountService;

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

    private final FixedDiscountService fixedDiscountService;
    private final VariableDiscountService variableDiscountService;
    private final LoyaltyDiscountService loyaltyDiscountService;

    /**
     * Расчет фиксированной скидки
     *
     * @param price             стоимость продукта
     * @param productCategoryId ID категории продукта
     * @param clientCategoryId  ID категории клиента
     * @return Стоимость с учетом скидок
     */
    @GetMapping("/fixed")
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
    @GetMapping("/variable")
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
     * Расчет скидки с учетом лояльности
     *
     * @param userId            ID пользователя
     * @param price             стоимость продукта
     * @return Стоимость с учетом лояльности пользователя
     */
    @GetMapping("/loyalty")
    @Operation(
            summary = "Рассчитать скидку по лояльности",
            description = "Возвращает размер скидки для пользователя на основе его статуса лояльности"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный расчет скидки с учетом лояльности"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    public BigDecimal calculateLoyaltyDiscount(
            @RequestParam("user_id") @Parameter(description = "ID пользователя") Long userId,
            @RequestParam("price") @Parameter(description = "Сумма, для которой предоставляется скидка") BigDecimal price
    ) {
        return loyaltyDiscountService.calculateDiscount(price, userId);
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

    @ExceptionHandler(FeignException.class)
    public ProblemDetail feignClientException(FeignException e) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.valueOf(e.status()),
                e.getMessage()
        );
    }
}
