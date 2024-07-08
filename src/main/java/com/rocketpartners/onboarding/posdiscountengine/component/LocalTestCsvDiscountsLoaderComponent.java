package com.rocketpartners.onboarding.posdiscountengine.component;

import com.rocketpartners.onboarding.commons.utils.FileLineReader;
import com.rocketpartners.onboarding.posdiscountengine.ApplicationProperties;
import com.rocketpartners.onboarding.posdiscountengine.service.DiscountService;
import lombok.NonNull;

import java.util.List;

import static com.rocketpartners.onboarding.commons.model.Discount.DiscountType;

/**
 * A component that loads discounts from a CSV file.
 */
public class LocalTestCsvDiscountsLoaderComponent implements DiscountsLoaderComponent {

    /**
     * Get the application properties. Package-private for testing purposes.
     *
     * @return the application properties
     */
    ApplicationProperties getProps() {
        return new ApplicationProperties();
    }

    /**
     * Get a CSV file reader. Package-private for testing purposes. Package-private for testing purposes.
     *
     * @return the CSV file reader
     */
    FileLineReader getFileReader() {
        return new FileLineReader();
    }

    @Override
    public void loadDiscounts(@NonNull DiscountService discountService) {
        ApplicationProperties props = getProps();
        String csvFilePath = props.getProperty("test.discounts.csv.file.path");
        List<String[]> csvLines = getFileReader().read(csvFilePath, ",");
        csvLines.forEach(it -> {
            if (it.length != 3) {
                throw new RuntimeException("Invalid CSV file format. Expected 2 fields per line. Invalid line: " + it);
            }
            String itemUpc = it[0];
            DiscountType discountType = DiscountType.valueOf(it[1]);
            int discountValue = Integer.parseInt(it[2]);
            discountService.putDiscount(itemUpc, discountType, discountValue);
        });
    }
}
