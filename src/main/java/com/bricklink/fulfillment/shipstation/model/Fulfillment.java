package com.bricklink.fulfillment.shipstation.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Fulfillment {
    private Long fulfillmentId;
    private Long orderId;
    private String orderNumber;
    private String userId;
    private String customerEmail;
    private String trackingNumber;
    private LocalDateTime createDate;
    private LocalDateTime shipDate;
    private LocalDateTime voidDate;
    private LocalDateTime deliveryDate;
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
    @JsonRootName("fulfillments")
    public static class FulfillmentList {
        private List<Fulfillment> fulfillments;
    }
}
