package com.bricklink.fulfillment.api.bricklink;

import com.bricklink.api.rest.client.BricklinkRestClient;
import com.bricklink.api.rest.client.ParamsBuilder;
import com.bricklink.api.rest.model.v1.BricklinkResource;
import com.bricklink.api.rest.model.v1.Order;
import com.bricklink.api.rest.model.v1.OrderItem;
import com.bricklink.api.rest.model.v1.OrderStatus;
import com.bricklink.fulfillment.api.OrderService;
import com.bricklink.fulfillment.api.ReferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class BricklinkOrderService implements OrderService<Order, OrderItem> {
    private  static final String NO_REMARKS = null;
    private  static final boolean DO_NOT_FILE = false;
    private static final Boolean CC_ME = true;
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

        ordersResource = bricklinkRestClient.getOrders(new ParamsBuilder().of("direction", "in")
                                                                          .get(), Arrays.asList("Packed"));
        List<Order> packedOrders = ordersResource.getData();
        orders.addAll(packedOrders);

        ordersResource = bricklinkRestClient.getOrders(new ParamsBuilder().of("direction", "in")
                                                                          .of("filed", false)
                                                                          .get(), Arrays.asList("Cancelled"));
        List<Order> cancelledOrders = ordersResource.getData();
        orders.addAll(cancelledOrders);

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
                                  .collect(Collectors.toList());
    }

    @Override
    public boolean isDomestic(Order order) {
        return referenceService.isDomestic(referenceService.lookupCountry(order.getShipping()
                                                                               .getAddress()
                                                                               .getCountry_code()));
    }

    @Override
    public Order markShipped(Order order) {
        if (order.isNotShipped()) {
            Order orderUpdate = getOrderForUpdate(order, NO_REMARKS, DO_NOT_FILE);
            bricklinkRestClient.updateOrder(order.getOrder_id(), orderUpdate);
            bricklinkRestClient.updateOrderStatus(order.getOrder_id(), OrderStatus.SHIPPED);
            Order updatedOrder = bricklinkRestClient.getOrder(order.getOrder_id()).getData();
            if (driveThruNotSent(updatedOrder)) {
                // if drive thru not sent, send drive thru!
                bricklinkRestClient.sendDriveThru(updatedOrder.getOrder_id(), CC_ME);
            }
        }
        return order;
    }

    private Order getOrderForUpdate(Order order, String remarks, boolean setIsFiled) {
        Order orderUpdate = new Order();
        orderUpdate.setShipping(order.getShipping());
        orderUpdate.setCost(order.getCost());
        orderUpdate.setRemarks(remarks);
        orderUpdate.setIs_filed(setIsFiled);
        return orderUpdate;
    }

    private boolean driveThruSent(Order order) {
        return Optional.ofNullable(order.getSent_drive_thru()).orElse(false);
    }

    private boolean driveThruNotSent(Order order) {
        return !driveThruSent(order);
    }
}
