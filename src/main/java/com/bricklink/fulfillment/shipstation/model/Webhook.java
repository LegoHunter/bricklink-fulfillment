package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Webhook {
    private String resource_url;    // This URL can be used to get the resource which triggered the webhook. 200 character limit. The URL can be accessed with ShipStation API Basic Authentication credentials.
    private String resource_type;   // The event type that triggered the webhook. Will be one of the following values: ORDER_NOTIFY, ITEM_ORDER_NOTIFY, SHIP_NOTIFY, ITEM_SHIP_NOTIFY
}
