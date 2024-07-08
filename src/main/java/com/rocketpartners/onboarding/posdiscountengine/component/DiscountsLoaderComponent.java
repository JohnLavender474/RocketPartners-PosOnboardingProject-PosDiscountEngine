package com.rocketpartners.onboarding.posdiscountengine.component;

import com.rocketpartners.onboarding.posdiscountengine.service.DiscountService;
import lombok.NonNull;

/**
 * Interface for components that load the discounts into the discount service.
 */
public interface DiscountsLoaderComponent {

    /**
     * Load the discounts into the discount service.
     *
     * @param discountService the discount service to load the discounts into
     */
    void loadDiscounts(@NonNull DiscountService discountService);
}
