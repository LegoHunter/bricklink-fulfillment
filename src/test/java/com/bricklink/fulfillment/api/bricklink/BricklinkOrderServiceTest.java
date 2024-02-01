package com.bricklink.fulfillment.api.bricklink;

import com.bricklink.api.rest.model.v1.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BricklinkOrderServiceTest {

    @Test
    void isPaid() {
        Order order = new Order();

        order.setStatus("PENDING");
        assertThat(order.isPaid()).isFalse();
        order.setStatus("UPDATED");
        assertThat(order.isPaid()).isFalse();
        order.setStatus("PROCESSING");
        assertThat(order.isPaid()).isFalse();
        order.setStatus("READY");
        assertThat(order.isPaid()).isFalse();
        order.setStatus("PAID");
        assertThat(order.isPaid()).isTrue();
        order.setStatus("PACKED");
        assertThat(order.isPaid()).isTrue();
        order.setStatus("SHIPPED");
        assertThat(order.isPaid()).isTrue();
        order.setStatus("RECEIVED");
        assertThat(order.isPaid()).isTrue();
        order.setStatus("COMPLETED");
        assertThat(order.isPaid()).isTrue();
        order.setStatus("CANCELLED");
        assertThat(order.isPaid()).isFalse();
    }
}