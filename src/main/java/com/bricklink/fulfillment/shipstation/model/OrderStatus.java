package com.bricklink.fulfillment.shipstation.model;

public enum OrderStatus {
    AWAITING_PAYMENT,
    AWAITING_SHIPMENT,
    SHIPPED,
    ON_HOLD,
    CANCELLED;

    public final String label;

    OrderStatus() {
        this.label = this.name().toLowerCase();
    }
}
