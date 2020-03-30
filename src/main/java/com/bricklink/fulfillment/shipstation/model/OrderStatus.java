package com.bricklink.fulfillment.shipstation.model;

public enum OrderStatus {
    NONE,
    CANCELLED,
    AWAITING_PAYMENT,
    AWAITING_SHIPMENT,
    SHIPPED,
    ON_HOLD;

    public final String label;

    OrderStatus() {
        this.label = this.name().toLowerCase();
    }
}
