package com.rocketpartners.onboarding.posdiscountengine.controller;

import com.rocketpartners.onboarding.commons.model.Discount;
import com.rocketpartners.onboarding.commons.model.DiscountComputation;
import com.rocketpartners.onboarding.commons.model.TransactionDto;
import com.rocketpartners.onboarding.posdiscountengine.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for discounts.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    /**
     * [GET] Get the discounts.
     *
     * @return the discounts
     */
    @GetMapping
    public ResponseEntity<Map<String, Discount>> getDiscounts() {
        return ResponseEntity.ok(discountService.getDiscounts());
    }

    /**
     * [POST] Compute the discount amount for a transaction.
     *
     * @param transaction the transaction
     * @return the discount amount
     */
    @PostMapping("/compute")
    public ResponseEntity<DiscountComputation> computeDiscountAmount(@RequestBody TransactionDto transaction) {
        DiscountComputation computation = discountService.computeDiscountAmount(transaction);
        return ResponseEntity.ok(computation);
    }
}
