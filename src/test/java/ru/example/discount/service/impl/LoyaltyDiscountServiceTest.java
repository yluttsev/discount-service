package ru.example.discount.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.example.discount.dto.UserTotalSpentDto;
import ru.example.discount.entity.LoyaltyLevel;
import ru.example.discount.feign.UserServiceClient;
import ru.example.discount.repository.LoyaltyLevelRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoyaltyDiscountServiceTest {

    @Mock
    private LoyaltyLevelRepository loyaltyLevelRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private LoyaltyDiscountService loyaltyDiscountService;

    @Test
    void calculateDiscount_shouldReturnPriceWithLoyaltyDiscount() {
        when(loyaltyLevelRepository.findByTotalAmount(any(BigDecimal.class)))
                .thenReturn(Optional.of(new LoyaltyLevel(2L, BigDecimal.valueOf(5001), BigDecimal.valueOf(10000), (short) 3)));
        when(userServiceClient.getUserTotalSpent(anyLong())).thenReturn(new UserTotalSpentDto(BigDecimal.valueOf(6000)));

        BigDecimal result = loyaltyDiscountService.calculateDiscount(BigDecimal.valueOf(1000), 1L);
        assertEquals(BigDecimal.valueOf(970).setScale(2, RoundingMode.HALF_UP), result);
    }

    @Test
    void calculateDiscount_shouldReturnPriceWithoutDiscounts() {
        when(loyaltyLevelRepository.findByTotalAmount(any(BigDecimal.class)))
                .thenReturn(Optional.of(new LoyaltyLevel(1L, BigDecimal.ZERO, BigDecimal.valueOf(5000), (short) 0)));
        when(userServiceClient.getUserTotalSpent(anyLong())).thenReturn(new UserTotalSpentDto(BigDecimal.valueOf(3000)));

        BigDecimal result = loyaltyDiscountService.calculateDiscount(BigDecimal.valueOf(1000), 1L);
        assertEquals(BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_UP), result);
    }
}
