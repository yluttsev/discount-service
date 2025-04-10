package ru.example.discount.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record UserTotalSpentDto(

        @JsonProperty("total_spent")
        BigDecimal totalSpent
) {
}
