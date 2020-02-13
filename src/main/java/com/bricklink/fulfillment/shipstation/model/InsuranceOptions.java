package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InsuranceOptions {
    private String provider;        // Preferred Insurance provider. Available options: "shipsurance", "carrier", or "provider". The "provider" option is used to indicate that a shipment was insured by a third party other than ShipSurance or the carrier. The insurance is handled outside of ShipStation, and will not affect the cost of processing the label.
    private Boolean insureShipment; // Indicates whether shipment should be insured.
    private Double insuredValue;    // Value to insure.
}
