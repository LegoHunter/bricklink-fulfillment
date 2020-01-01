package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InternationalOptions {
    private String contents;            // Contents of international shipment. Available options are: "merchandise", "documents", "gift", "returned_goods", or "sample"
    private CustomsItem[] customsItems; // An array of customs items. Please note: If you wish to supply customsItems in the CreateOrder call and have the values not be overwritten by ShipStation, you must have the International Settings > Customs Declarations set to "Leave blank (Enter Manually)" in the UI: https://ss.shipstation.com/#/settings/international
    private String nonDelivery;         // Non-Delivery option for international shipment. Available options are: "return_to_sender" or "treat_as_abandoned". Please note: If the shipment is created through the Orders/CreateLabelForOrder endpoint and the nonDelivery field is not specified then value defaults based on the International Setting in the UI. If the call is being made to the Shipments/CreateLabel endpoint and the nonDelivery field is not specified then the value will default to "return_to_sender".
}
