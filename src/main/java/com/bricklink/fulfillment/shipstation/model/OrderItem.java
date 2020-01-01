package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItem {
private Integer orderItemId;		// The system generated identifier for the OrderItem. This is a read-only field.
private String lineItemKey;			// An identifier for the OrderItem in the originating system.
private String sku;					// The SKU (stock keeping unit) identifier for the product associated with this line item.
private String name;				// The name of the product associated with this line item. Cannot be null
private String imageUrl;			// The public URL to the product image.
private Weight weight;				// The weight of a single item.
private Integer quantity;			// The quantity of product ordered.
private Double unitPrice;			// The sell price of a single item specified by the order source.
private Double taxAmount;			// The tax price of a single item specified by the order source.
private Double shippingAmount;		// The shipping amount or price of a single item specified by the order source.
private String warehouseLocation;   // The location of the product within the seller's warehouse (e.g. Aisle 3, Shelf A, Bin 5)
private ItemOption[] options;		//     productId	Integer	The identifier for the Product Resource associated with this OrderItem.
private String fulfillmentSku;		// The fulfillment SKU associated with this OrderItem if the fulfillment provider requires an identifier other then the SKU.
private Boolean adjustment;			// Indicates that the OrderItem is a non-physical adjustment to the order (e.g. a discount or promotional code)
private String upc;					// The Universal Product Code associated with this OrderItem.
private Date createDate;			// The timestamp the orderItem was created in ShipStation's database. Read-Only.
private Date modifyDate;			// The timestamp the orderItem was modified in ShipStation. modifyDate will equal createDate until a modification is made. Read-Only.
}
