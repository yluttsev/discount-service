package ru.example.discount.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.discount.dto.UserTotalSpentDto;
import ru.example.discount.entity.LoyaltyLevel;
import ru.example.discount.feign.UserServiceClient;
import ru.example.discount.repository.LoyaltyLevelRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoyaltyDiscountService {

    private final UserServiceClient userServiceClient;
    private final LoyaltyLevelRepository loyaltyLevelRepository;

    public BigDecimal calculateDiscount(BigDecimal price, long userId) {
        UserTotalSpentDto userTotalSpent = userServiceClient.getUserTotalSpent(userId);
        Optional<LoyaltyLevel> loyaltyLevel = loyaltyLevelRepository.findByTotalAmount(userTotalSpent.totalSpent());
        return loyaltyLevel.map(
                level -> price.subtract(price.multiply(BigDecimal.valueOf(level.getPercent() / 100.0)))
                        .setScale(2, RoundingMode.HALF_UP)
        ).orElse(price);
    }
}
