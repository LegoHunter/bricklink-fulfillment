package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Dimensions {
    private Integer length; // Length of package.
    private Integer width;  // Width of package.
    private Integer height; // Height of package.
    private String units;   // Units of measurement. Allowed units are: "inches", or "centimeters"
}
