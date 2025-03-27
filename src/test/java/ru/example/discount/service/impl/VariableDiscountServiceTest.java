package ru.example.discount.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import ru.example.discount.entity.ClientCategory;
import ru.example.discount.entity.Discount;
import ru.example.discount.entity.ProductCategory;
import ru.example.discount.entity.enums.DiscountType;
import ru.example.discount.repository.DiscountRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VariableDiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private VariableDiscountService variableDiscountService;

    @Test
    void calculateDiscount_shouldReturnOriginalPriceWhenNoDiscounts() {
        ReflectionTestUtils.setField(variableDiscountService, "isDiscountAggregate", false);

        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(), any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of()));

        BigDecimal result = variableDiscountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L);

        assertEquals(BigDecimal.valueOf(100), result);
    }

    @Test
    void calculateDiscount_shouldApplySingleDiscount() {
        ReflectionTestUtils.setField(variableDiscountService, "isDiscountAggregate", false);

        Discount discount = createDiscount((short) 5, (short) 15);

        BigDecimal minExpected = BigDecimal.valueOf(85);
        BigDecimal maxExpected = BigDecimal.valueOf(95);

        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(), eq(PageRequest.of(0, 1))
        )).thenReturn(new PageImpl<>(List.of(discount)));

        BigDecimal result = variableDiscountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L);

        assertTrue(result.compareTo(minExpected) >= 0,
                "Цена " + result + " должна быть >= " + minExpected);
        assertTrue(result.compareTo(maxExpected) <= 0,
                "Цена " + result + " должна быть <= " + maxExpected);
    }

    @Test
    void calculateDiscount_shouldAggregateDiscountsWhenAggregationEnabled() {
        ReflectionTestUtils.setField(variableDiscountService, "isDiscountAggregate", true);

        BigDecimal minExpected = BigDecimal.valueOf(60);
        BigDecimal maxExpected = BigDecimal.valueOf(80);

        Discount discount1 = createDiscount((short) 10, (short) 20);
        Discount discount2 = createDiscount((short) 10, (short) 20);

        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(DiscountType.class), eq(Pageable.unpaged())
        )).thenReturn(new PageImpl<>(List.of(discount1, discount2)));

        BigDecimal result = variableDiscountService.calculateDiscount(
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
