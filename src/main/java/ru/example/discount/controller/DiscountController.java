package ru.example.discount.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.example.discount.service.DiscountService;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService fixedDiscountService;

    @GetMapping("/fixed")
    public ResponseEntity<BigDecimal> calculateFixedDiscount(@RequestParam("price") BigDecimal price,
                                                             @RequestParam("product_category") long productCategoryId,
                                                             @RequestParam("client_category") long clientCategoryId) {
        log.info("GET /discount/fixed: price = {}, productCategoryId = {}, clientCategoryID = {}",
                price,
                productCategoryId,
                clientCategoryId
        );
        return ResponseEntity.ok().body(fixedDiscountService.calculateDiscount(price, productCategoryId, clientCategoryId));
    }
}
