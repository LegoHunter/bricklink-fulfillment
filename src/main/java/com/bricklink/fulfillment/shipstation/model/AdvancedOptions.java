package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdvancedOptions {
    private Long warehouseId; // Specifies the warehouse where to the order is to ship from. If the order was fulfilled using a fill provider, no warehouse is attached to these orders and will result in a null value being returned. *Please see note below
    private Boolean nonMachinable; // Specifies whether the order is non-machinable.
    private Boolean saturdayDelivery; // Specifies whether the order is to be delivered on a Saturday.
    private Boolean containsAlcohol; // Specifies whether the order contains alcohol.
    private Long storeId; // ID of store that is associated with the order. If not specified in the CreateOrder call either to create or update an order, ShipStation will default to the first manual store on the account. Can only be specified during order creation.
    private String customField1; // Field that allows for custom data to be associated with an order. *Please see note below
    private String customField2; // Field that allows for custom data to be associated with an order. *Please see note below
    private String customField3; // Field that allows for custom data to be associated with an order. *Please see note below
    private String source; // Identifies the original source/marketplace of the order. *Please see note below
    private Boolean mergedOrSplit; // Read-Only: Returns whether or not an order has been merged or split with another order. Read Only
    private Long[] mergedIds; // Read-Only: private of Array; // orderIds. Each orderId identifies an order that was merged with the associated order. Read Only
    private Long parentId; // Read-Only: If an order has been split, it will return the Parent ID of the order with which it has been split. If the order has not been split, this field will return null. Read Only
    private String billToParty; // Identifies which party to bill. Possible values: "my_account", **"my_other_account" (see ** note below), "recipient", "third_party". billTo values can only be used when creating/updating orders.
    private String billToAccount; // Account number of billToParty. billTo values can only be used when creating/updating orders.
    private String billToPostalCode; // Postal Code of billToParty. billTo values can only be used when creating/updating orders.
    private String billToCountryCode; // Country Code of billToParty. billTo values can only be used when creating/updating orders.
    private String billToMyOtherAccount; // When using my_other_account billToParty value, the shippingProviderId value associated with the desired account. Make a List Carriers call to obtain shippingProviderId values.    
}
