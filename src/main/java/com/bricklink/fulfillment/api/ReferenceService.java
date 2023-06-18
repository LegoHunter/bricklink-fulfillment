package com.bricklink.fulfillment.api;

import com.bricklink.fulfillment.BricklinkFulfillmentException;
import com.bricklink.fulfillment.shipstation.model.Country;

import java.util.Map;

public interface ReferenceService {
    Map<String, Country> getAllCountries();

    boolean isDomestic(Country country);

    default boolean isInternational(Country country) {
        return !isDomestic(country);
    }

    default Country lookupCountry(String code) {
        return getAllCountries().values()
                                .stream()
                                .filter(c -> c.hasCode(code))
                                .findFirst()
                                .orElseThrow(() -> new BricklinkFulfillmentException("Country code [%s] not found".formatted(code)));
    }
}
