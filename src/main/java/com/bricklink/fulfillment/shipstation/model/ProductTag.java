package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductTag {
    private Integer tagId;  // The system generated identifier for the product tag.
    private String name;    // Name or description for the product tag.
}
