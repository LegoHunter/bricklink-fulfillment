package com.bricklink.fulfillment.api.shipstation;

import com.bricklink.fulfillment.BricklinkFulfillmentException;
import com.bricklink.fulfillment.api.OrderService;
import com.bricklink.fulfillment.shipstation.model.OrderItem;
import com.bricklink.fulfillment.shipstation.model.OrderStatus;
import com.bricklink.fulfillment.shipstation.model.ShipStationOrder;
import com.bricklink.fulfillment.shipstation.model.Shipment;
import com.bricklink.fulfillment.shipstation.model.Tracking;
import com.bricklink.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.naming.OperationNotSupportedException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static com.bricklink.fulfillment.shipstation.model.ShipStationOrder.OrdersList;

@RequiredArgsConstructor
@Slf4j
public class ShipStationOrderService implements OrderService<ShipStationOrder, OrderItem> {
    private final OrdersAPI ordersAPI;
    private static final String DOMESTIC_TRACKING_URL = "https://tools.usps.com/go/TrackConfirmAction.action?tLabels=%s";
    private static final String INTERNATIONAL_TRACKING_URL = "http://parcelsapp.com/en/tracking/%s";
    private final ShipmentsAPI shipmentsAPI;

    @Override
    public List<ShipStationOrder> getOrdersForFulfillment() {
        return null;
    }

    @Override
    public ShipStationOrder getOrder(String orderId) {
        ShipStationOrder order;
        // Find existing Shipstation order (if it exists)
        OrdersList shipStationOrders = ordersAPI.getOrders(new ParamsBuilder().of("orderNumber", "BL-%s".formatted(orderId))
                                                                              .get());
        int orderCount = shipStationOrders.getOrders()
                                          .size();
        if (orderCount < 1) {
            order = new ShipStationOrder();
            order.setOrderStatus(OrderStatus.NONE.label);
        } else if (orderCount == 1) {
            order = shipStationOrders.getOrders()
                                     .get(0);
        } else {
            throw new BricklinkFulfillmentException("Found [%d] orders for order number [%s] in Shipstation".formatted(orderCount, "BL-%s".formatted(orderId)));
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

    @Override
    public ShipStationOrder markShipped(ShipStationOrder shipStationOrder) {
        throw new BricklinkFulfillmentException(new OperationNotSupportedException("Cannot mark a ShipStation order as shipped"));
    }

    public Tracking getOrderTracking(ShipStationOrder shipStationOrder) {
        Shipment.ShipmentList shipments = shipmentsAPI.getShipments(new ParamsBuilder().of("orderId", shipStationOrder.getOrderId())
                                                                                       .get());
        return shipments.getShipments()
                        .stream()
                        .filter(s -> s.getVoided().equals(Boolean.FALSE))
                        .map(s -> Tracking.builder()
                                          .trackingNumber(s.getTrackingNumber())
                                          .dateShipped(DateUtils.toZonedDateTime(s.getCreateDate()))
                                          .build())
                        .peek(t -> t.setTrackingURL(getTrackingUrl(shipStationOrder, t.getTrackingNumber())))
                        .findFirst()
                        .orElseThrow(() -> new BricklinkFulfillmentException(String.format("Unable to get tracking for ShipStation Order [%s]", shipStationOrder.getOrderId())));
    }

    public URL getTrackingUrl(ShipStationOrder shipStationOrder, String trackingNumber) {
        try {
            URL url = null;
            if (isDomestic(shipStationOrder)) {
                url = new URL(DOMESTIC_TRACKING_URL.formatted(trackingNumber));
            } else {
                url = new URL(INTERNATIONAL_TRACKING_URL.formatted(trackingNumber));
            }
            return url;
        } catch (MalformedURLException e) {
            throw new BricklinkFulfillmentException(e);
        }
    }

    public boolean orderIsNew(ShipStationOrder shipStationOrder) {
        return !Optional.ofNullable(shipStationOrder.getOrderId())
                        .isPresent();
    }
}
