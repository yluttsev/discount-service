package ru.example.discount.service.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

@ExtendWith(MockitoExtension.class)
class FixedDiscountServiceTest {

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
        Discount discount = createDiscount((short)10, (short)10);
        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(), eq(PageRequest.of(0, 1))
        )).thenReturn(new PageImpl<>(List.of(discount)));

        BigDecimal result = discountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L);

        assertEquals(BigDecimal.valueOf(90), result);
    }

    @Test
    void calculateDiscount_shouldAggregateDiscountsWhenAggregationEnabled() {

        ReflectionTestUtils.setField(discountService, "discount.aggregation", true);

        Discount discount1 = createDiscount((short)5, (short)5);
        Discount discount2 = createDiscount((short)10, (short)10);

        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(), eq(PageRequest.of(0, 1))
        )).thenReturn(new PageImpl<>(List.of(discount1, discount2)));

        BigDecimal result = discountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L
        );

        assertEquals(BigDecimal.valueOf(85), result);
    }


    private Discount createDiscount(short minValue, short maxValue) {
        return Discount.builder()
                .id(1L)
                .minValue(minValue)
                .maxValue(maxValue)
                .discountType(DiscountType.FIXED)
                .productCategory(new ProductCategory(1L, "EXAMPLE"))
                .clientCategory(new ClientCategory(1L, "EXAMPLE"))
                .build();
    }
}