package com.bricklink.fulfillment.api.shipstation;

import com.bricklink.fulfillment.shipstation.model.Shipment.ShipmentList;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

public interface Shipments {
    @RequestLine("GET /shipments")
    ShipmentList getShipments();

    @RequestLine("GET /shipments?recipientName={recipientName}&recipientCountryCode={recipientCountryCode}&orderNumber={orderNumber}&orderId={orderId}&carrierCode={carrierCode}&serviceCode={serviceCode}&trackingNumber={trackingNumber}&createDateStart={createDateStart}&createDateEnd={createDateEnd}&shipDateStart={shipDateStart}&shipDateEnd={shipDateEnd}&voidDateStart={voidDateStart}&voidDateEnd={voidDateEnd}&storeId={storeId}&includeShipmentItems={includeShipmentItems}&sortBy={sortBy}&sortDir={sortDir}&page={page}&pageSize={pageSize}")
    ShipmentList getShipments(@QueryMap Map<String, Object> params);
}
