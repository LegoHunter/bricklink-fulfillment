package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Weight {
    private Integer value;          // weight value.
    private String units;           // units of weight. Allowed units are: "pounds", "ounces", or "grams"
    private Integer WeightUnits;    // (read only) A numeric value that is equivalent to the above units field.
}
