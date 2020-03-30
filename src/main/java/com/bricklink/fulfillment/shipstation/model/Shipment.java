package com.bricklink.fulfillment.shipstation.model;

import com.bricklink.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Shipment {
    private Long fulfillmentId;
    private Long orderId;
    private String orderNumber;
    private String userId;
    private String customerEmail;
    private String trackingNumber;
    private LocalDateTime createDate;
    private Date shipDate;
    @JsonDeserialize(using = DateUtils.ZonedDateTimeDeserializer.class)
    private ZonedDateTime voidDate;
    @JsonDeserialize(using = DateUtils.ZonedDateTimeDeserializer.class)
    private ZonedDateTime deliveryDate;
    private String carrierCode;
    private String fulfillmentProviderCode;
    private String fulfillmentServiceCode;
    private Double fulfillmentFee;
    private Boolean voidRequested;
    private Boolean voided;
    private Boolean marketplaceNotified;
    private String notifyErrorMessage;
    private Address shipTo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonRootName("shipments")
    public static class ShipmentList {
        private List<Shipment> shipments;
    }
}
