package ru.example.discount.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import ru.example.discount.entity.ClientCategory;
import ru.example.discount.entity.Discount;
import ru.example.discount.entity.ProductCategory;
import ru.example.discount.entity.enums.DiscountType;
import ru.example.discount.repository.DiscountRepository;
import ru.example.discount.service.DiscountService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VariableDiscountServiceTest {
    @MockitoBean
    private DiscountRepository discountRepository;

    @MockitoBean
    private DiscountService discountService;

    @Test
    void calculateDiscount_shouldReturnOriginalPriceWhenNoDiscounts() {
        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(), any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of()));

        BigDecimal result = discountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L);

        assertEquals(BigDecimal.valueOf(100), result);
    }

    @Test
    void calculateDiscount_shouldApplySingleDiscount() {
        Discount discount = createDiscount((short)5, (short)15);

        BigDecimal minExpected = BigDecimal.valueOf(85);
        BigDecimal maxExpected = BigDecimal.valueOf(95);

        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(), eq(PageRequest.of(0, 1))
        )).thenReturn(new PageImpl<>(List.of(discount)));

        BigDecimal result = discountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L);

        assertTrue(result.compareTo(minExpected) >= 0,
                "Цена " + result + " должна быть >= " + minExpected);
        assertTrue(result.compareTo(maxExpected) <= 0,
                "Цена " + result + " должна быть <= " + maxExpected);
    }

    @Test
    void calculateDiscount_shouldAggregateDiscountsWhenAggregationEnabled() {

        ReflectionTestUtils.setField(discountService, "discount.aggregation", true);

        BigDecimal minExpected = BigDecimal.valueOf(60);
        BigDecimal maxExpected = BigDecimal.valueOf(80);

        Discount discount1 = createDiscount((short)10, (short)20);
        Discount discount2 = createDiscount((short)10, (short)20);

        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(), eq(PageRequest.of(0, 1))
        )).thenReturn(new PageImpl<>(List.of(discount1, discount2)));

        BigDecimal result = discountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L
        );

        assertTrue(result.compareTo(minExpected) >= 0,
                "Цена " + result + " должна быть >= " + minExpected);
        assertTrue(result.compareTo(maxExpected) <= 0,
                "Цена " + result + " должна быть <= " + maxExpected);
    }


    private Discount createDiscount(short minValue, short maxValue) {
        return Discount.builder()
                .id(1L)
                .minValue(minValue)
                .maxValue(maxValue)
                .discountType(DiscountType.VARIABLE)
                .productCategory(new ProductCategory(1L, "EXAMPLE"))
                .clientCategory(new ClientCategory(1L, "EXAMPLE"))
                .build();
    }
}
