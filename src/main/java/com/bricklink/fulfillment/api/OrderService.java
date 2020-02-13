package com.bricklink.fulfillment.api;

import java.util.List;

public interface OrderService<T, U> {
    List<T> getOrdersForFulfillment();

    T getOrder(String orderId);

    List<U> getOrderItems(String orderId);

    boolean isDomestic(T order);

    default String getService(T t) {
        if (isDomestic(t)) {
            return "usps_priority_mail";
        } else {
            return "usps_priority_mail_international";
        }
    }

    default boolean isInternational(T t) {
        return !isDomestic(t);
    }
}
