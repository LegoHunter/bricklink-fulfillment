package com.bricklink.fulfillment.shipstation.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    private Long customerId;
    private Date createDate;
    private Date modifyDate;
    private String name;
    private String company;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String postalCode;
    private String countryCode;
    private String phone;
    private String email;
    private String addressVerified;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class MarketplaceUsername {
        private String customerUserId;
        private Long customerId;
        private Date createDate;
        private Date modifyDate;
        private Integer marketplaceId;
        private String marketplace;
        private String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Tag {
        private String tagId;
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonRootName("customers")
    public static class CustomersList {
        private List<Customer> customers;
    }
}
