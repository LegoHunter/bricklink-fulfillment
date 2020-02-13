package com.bricklink.fulfillment.api.shipstation;

import com.bricklink.fulfillment.BricklinkFulfillmentException;
import com.bricklink.fulfillment.api.OrderService;
import com.bricklink.fulfillment.shipstation.model.OrderItem;
import com.bricklink.fulfillment.shipstation.model.ShipStationOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.bricklink.fulfillment.shipstation.model.ShipStationOrder.OrdersList;

@RequiredArgsConstructor
@Slf4j
public class ShipStationOrderService implements OrderService<ShipStationOrder, OrderItem> {
    private final OrdersAPI ordersAPI;

    @Override
    public List<ShipStationOrder> getOrdersForFulfillment() {
        return null;
    }

    @Override
    public ShipStationOrder getOrder(String orderId) {
        ShipStationOrder order;
        // Find existing Shipstation order (if it exists)
        OrdersList shipStationOrders = ordersAPI.getOrders(new ParamsBuilder().of("orderNumber", String.format("BL-%s", orderId))
                                                                              .get());
        int orderCount = shipStationOrders.getOrders()
                                          .size();
        if (orderCount < 1) {
            order = new ShipStationOrder();
        } else if (orderCount == 1) {
            order = shipStationOrders.getOrders()
                                     .get(0);
        } else {
            throw new BricklinkFulfillmentException(String.format("Found [%d] orders for order number [%s] in Shipstation", orderCount, String.format("BL-%s", orderId)));
        }
        return order;
    }

    @Override
    public List<OrderItem> getOrderItems(String orderId) {
        return null;
    }

    @Override
    public boolean isDomestic(ShipStationOrder order) {
        return false;
    }
}
