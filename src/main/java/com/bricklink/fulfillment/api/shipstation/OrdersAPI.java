package com.bricklink.fulfillment.api.shipstation;

import com.bricklink.fulfillment.shipstation.model.ShipStationOrder;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

import static com.bricklink.fulfillment.shipstation.model.ShipStationOrder.OrdersList;

public interface OrdersAPI {
    @RequestLine("GET /orders")
    OrdersList getOrders();

    @RequestLine("GET /orders?customerName={customerName}&itemKeyword={itemKeyword}&createDateStart={createDateStart}&createDateEnd={createDateEnd}&modifyDateStart={modifyDateStart}&modifyDateEnd={modifyDateEnd}&orderDateStart={orderDateStart}&orderDateEnd={orderDateEnd}&orderNumber={orderNumber}&orderStatus={orderStatus}&paymentDateStart={paymentDateStart}&paymentDateEnd={paymentDateEnd}&storeId={storeId}&sortBy={sortBy}&sortDir={sortDir}&page={page}&pageSize={pageSize}")
    OrdersList getOrders(@QueryMap Map<String, Object> params);

    @RequestLine("GET /orders/{orderId}")
    ShipStationOrder getOrder(@Param("orderId") Long orderId);

    @RequestLine("POST /orders/createorder")
    @Headers("Content-Type: application/json")
    ShipStationOrder createOrUpdateOrder(ShipStationOrder order);
}