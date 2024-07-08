package com.rocketpartners.onboarding.posdiscountengine.service;

import com.rocketpartners.onboarding.commons.model.Discount;
import com.rocketpartners.onboarding.commons.model.DiscountComputation;
import com.rocketpartners.onboarding.commons.model.LineItemDto;
import com.rocketpartners.onboarding.commons.model.TransactionDto;
import com.rocketpartners.onboarding.posdiscountengine.component.LocalTestCsvDiscountsLoaderComponent;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service for computing discounts.
 */
@Service
public class DiscountService {

    private final Map<String, Discount> discounts;

    /**
     * Create a new discount service. Loads the discounts from the local test CSV file.
     */
    public DiscountService() {
        discounts = new HashMap<>();
        new LocalTestCsvDiscountsLoaderComponent().loadDiscounts(this);
    }

    /**
     * Returns an unmodifiable map of discounts.
     *
     * @return the discounts
     */
    public Map<String, Discount> getDiscounts() {
        return Collections.unmodifiableMap(discounts);
    }

    /**
     * Compute the discount amount for a transaction.
     *
     * @param transaction the transaction
     * @return the discount amount
     */
    public DiscountComputation computeDiscountAmount(@NonNull TransactionDto transaction) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        Map<String, Discount> appliedDiscounts = new HashMap<>();
        Map<String, BigDecimal> results = new HashMap<>();
        for (LineItemDto it : transaction.getLineItemDtos()) {
            if (discounts.containsKey(it.getItemUpc())) {
                Discount discount = discounts.get(it.getItemUpc());
                BigDecimal computedDiscount = compute(it, discount);
                discountAmount = discountAmount.add(computedDiscount);
                results.put(it.getItemUpc(), computedDiscount);
                appliedDiscounts.put(it.getItemUpc(), discount);
            }
        }
        return new DiscountComputation(discountAmount, appliedDiscounts, results);
    }

    /**
     * Computes the amount to be subtracted from the subtotal.
     *
     * @param lineItem the line item
     * @param discount the discount to apply
     * @return the amount to be subtracted from the subtotal
     */
    private BigDecimal compute(@NonNull LineItemDto lineItem, @NonNull Discount discount) {
        int quantity = lineItem.getQuantity();
        BigDecimal unitPrice = lineItem.getUnitPrice();
        BigDecimal discountAmount = BigDecimal.ZERO;
        switch (discount.getType()) {
            case PCT_OFF -> {
                BigDecimal percentage = BigDecimal.valueOf((double) discount.getValue() / 100);
                discountAmount = unitPrice.multiply(percentage).multiply(BigDecimal.valueOf(quantity));
            }
            case XFOR -> {
                int x = discount.getValue();
                int freeItems = quantity / (x + 1);
                discountAmount = unitPrice.multiply(BigDecimal.valueOf(freeItems));
            }
        }
        return discountAmount;
    }

    /**
     * Put a discount to an item.
     *
     * @param itemUpc       the item upc
     * @param discountType  the discount type
     * @param discountValue the discount value
     */
    public void putDiscount(@NonNull String itemUpc, @NonNull Discount.DiscountType discountType, int discountValue) {
        discounts.put(itemUpc, new Discount(discountType, discountValue));
    }
}
