package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomsItem {
    private String customsItemId;        // Read Only field. When this field is not submitted in the Order/CreateOrder call, it will create a new customs line. If this field is included when submitting an order through Order/CreateOrder, then it will look for the corresponding customs line and update any values.
    private String description;          // A short description of the CustomsItem
    private Integer quantity;            // The quantity for this line item
    private Double value;                // The value (in USD) of the line item
    private String harmonizedTariffCode; // The Harmonized Commodity Code for this line item
    private String countryOfOrigin;      // The 2-character ISO country code where the item originated
}
