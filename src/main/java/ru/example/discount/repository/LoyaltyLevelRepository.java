package ru.example.discount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.example.discount.entity.LoyaltyLevel;

import java.math.BigDecimal;
import java.util.Optional;

public interface LoyaltyLevelRepository extends JpaRepository<LoyaltyLevel, Long> {

    @Query("from LoyaltyLevel ll where :total_amount between ll.minAmount and ll.maxAmount")
    Optional<LoyaltyLevel> findByTotalAmount(@Param("total_amount") BigDecimal totalAmount);
}
