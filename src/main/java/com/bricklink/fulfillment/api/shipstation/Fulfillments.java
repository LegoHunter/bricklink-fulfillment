package com.bricklink.fulfillment.api.shipstation;

import com.bricklink.fulfillment.shipstation.model.Fulfillment.FulfillmentList;
import feign.RequestLine;

public interface Fulfillments {
    @RequestLine("GET /fulfillments")
    FulfillmentList getFulfillments();
}
