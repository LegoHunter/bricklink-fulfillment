package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductCategory {
    private Integer categoryId; // The system generated identifier for the product category.
    private String name;        // Name or description for the product category.
}
