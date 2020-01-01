package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemOption {
    private String name;  // Name of item option. Example: "Size"
    private String value; // The value of the item option. Example: "Medium"
}
