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
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FixedDiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private FixedDiscountService fixedDiscountService;

    @Test
    void calculateDiscount_shouldReturnOriginalPriceWhenNoDiscounts() {
        ReflectionTestUtils.setField(fixedDiscountService, "isDiscountAggregate", false);

        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(), any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of()));

        BigDecimal result = fixedDiscountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L);

        assertEquals(BigDecimal.valueOf(100), result);
    }

    @Test
    void calculateDiscount_shouldApplySingleDiscount() {
        ReflectionTestUtils.setField(fixedDiscountService, "isDiscountAggregate", false);

        Discount discount = createDiscount((short) 10, (short) 10);
        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(), eq(PageRequest.of(0, 1))
        )).thenReturn(new PageImpl<>(List.of(discount)));

        BigDecimal result = fixedDiscountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L);

        assertEquals(BigDecimal.valueOf(90.0), result);
    }

    @Test
    void calculateDiscount_shouldAggregateDiscountsWhenAggregationEnabled() {
        ReflectionTestUtils.setField(fixedDiscountService, "isDiscountAggregate", true);

        Discount discount1 = createDiscount((short) 5, (short) 5);
        Discount discount2 = createDiscount((short) 10, (short) 10);

        when(discountRepository.findByClientCategoryAndProductCategory(
                anyLong(), anyLong(), any(DiscountType.class), eq(Pageable.unpaged())
        )).thenReturn(new PageImpl<>(List.of(discount1, discount2)));

        BigDecimal result = fixedDiscountService.calculateDiscount(
                BigDecimal.valueOf(100), 1L, 1L
        );

        assertEquals(BigDecimal.valueOf(85.5).setScale(2, RoundingMode.HALF_UP), result);
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