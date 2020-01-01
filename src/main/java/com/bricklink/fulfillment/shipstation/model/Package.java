package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Package {
    private String carrierCode;
    private String code;
    private String name;
    private boolean domestic;
    private boolean international;
}
