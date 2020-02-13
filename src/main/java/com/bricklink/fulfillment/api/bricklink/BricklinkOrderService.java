package com.bricklink.fulfillment.api.bricklink;

import com.bricklink.api.rest.client.BricklinkRestClient;
import com.bricklink.api.rest.client.ParamsBuilder;
import com.bricklink.api.rest.model.v1.BricklinkResource;
import com.bricklink.api.rest.model.v1.Order;
import com.bricklink.api.rest.model.v1.OrderItem;
import com.bricklink.fulfillment.BricklinkFulfillmentException;
import com.bricklink.fulfillment.api.OrderService;
import com.bricklink.fulfillment.api.ReferenceService;
import com.bricklink.fulfillment.shipstation.model.Country;
import com.bricklink.fulfillment.shipstation.model.ShipStationOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class BricklinkOrderService implements OrderService<Order, OrderItem> {
    private final BricklinkRestClient bricklinkRestClient;
    private final ReferenceService referenceService;

    @Override
    public List<Order> getOrdersForFulfillment() {
        BricklinkResource<List<Order>> ordersResource = bricklinkRestClient.getOrders(new ParamsBuilder().of("direction", "in")
                                                                                                         .get(), Arrays.asList("Pending"));
        List<Order> orders = ordersResource.getData();
        ordersResource = bricklinkRestClient.getOrders(new ParamsBuilder().of("direction", "in")
                                                                          .get(), Arrays.asList("Updated"));
        List<Order> updatedOrders = ordersResource.getData();
        orders.addAll(updatedOrders);

        ordersResource = bricklinkRestClient.getOrders(new ParamsBuilder().of("direction", "in")
                                                                          .get(), Arrays.asList("Ready"));
        List<Order> readyOrders = ordersResource.getData();
        orders.addAll(readyOrders);

        ordersResource = bricklinkRestClient.getOrders(new ParamsBuilder().of("direction", "in")
                                                                          .get(), Arrays.asList("Processing"));
        List<Order> processingOrders = ordersResource.getData();
        orders.addAll(processingOrders);

        ordersResource = bricklinkRestClient.getOrders(new ParamsBuilder().of("direction", "in")
                                                                          .get(), Arrays.asList("Paid"));
        List<Order> paidOrders = ordersResource.getData();
        orders.addAll(paidOrders);

        return orders;
    }

    @Override
    public Order getOrder(String orderId) {
        BricklinkResource<Order> orderResource = bricklinkRestClient.getOrder(orderId);
        return orderResource.getData();
    }

    @Override
    public List<OrderItem> getOrderItems(String orderId) {
        return bricklinkRestClient.getOrderItems(orderId)
                                  .getData()
                                  .stream()
                                  .flatMap(Collection::stream)
                                  .filter(oi -> oi.getItem()
                                                  .getType()
                                                  .equals("SET"))
                                  .collect(Collectors.toList());
    }

    @Override
    public boolean isDomestic(Order order) {
        return referenceService.isDomestic(referenceService.lookupCountry(order.getShipping()
                                                                               .getAddress()
                                                                               .getCountry_code()));
    }
}
