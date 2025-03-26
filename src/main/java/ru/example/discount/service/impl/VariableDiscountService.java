package ru.example.discount.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.example.discount.entity.Discount;
import ru.example.discount.entity.enums.DiscountType;
import ru.example.discount.repository.DiscountRepository;
import ru.example.discount.service.DiscountService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class VariableDiscountService implements DiscountService {

    private final DiscountRepository discountRepository;

    @Value("${discount.aggregation}")
    private boolean isDiscountAggregate;

    @Override
    public BigDecimal calculateDiscount(BigDecimal price, long productCategoryId, long clientCategoryId) {
        List<Discount> discounts = getDiscounts(productCategoryId, clientCategoryId);
        if (!discounts.isEmpty()) {
            return isDiscountAggregate ? calculateAggregatedDiscount(price, discounts) : calculateSingleDiscount(price, discounts.get(0));
        }
        return null;
    }

    private List<Discount> getDiscounts(long productCategoryId, long clientCategoryId) {
        if (isDiscountAggregate) {
            return discountRepository.findByClientCategoryAndProductCategory(
                            productCategoryId,
                            clientCategoryId,
                            DiscountType.VARIABLE,
                            Pageable.unpaged())
                    .getContent();
        }
        return discountRepository.findByClientCategoryAndProductCategory(
                productCategoryId,
                clientCategoryId,
                DiscountType.VARIABLE,
                PageRequest.of(0, 1)
        ).getContent();
    }

    private BigDecimal calculateAggregatedDiscount(BigDecimal price, List<Discount> discounts) {
        BigDecimal resultPrice = price;
        for (Discount discount : discounts) {
            BigDecimal multiplier = BigDecimal.ONE.subtract(BigDecimal.valueOf(randomDiscountValue(discount.getMinValue(), discount.getMaxValue()) / 100.0));
            resultPrice = resultPrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        }
        return resultPrice;
    }

    private BigDecimal calculateSingleDiscount(BigDecimal price, Discount discount) {
        return price.subtract(price.multiply(BigDecimal.valueOf(randomDiscountValue(discount.getMinValue(), discount.getMaxValue()) / 100.0)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private int randomDiscountValue(short minValue, short maxValue) {
        return ThreadLocalRandom.current().nextInt(
                minValue,
                maxValue
        );
    }

}