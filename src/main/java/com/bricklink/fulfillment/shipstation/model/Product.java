package com.bricklink.fulfillment.shipstation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    private Integer productId;					// The system generated identifier for the product. This is a read-only field.
    private String sku;					        // Stock keeping Unit. A user-defined value for a product to help identify the product. It is suggested that each product should contain a unique SKU.
    private String name;					    // Name or description of the product.
    private Double price;					    // The unit price of the product.
    private Double defaultCost;					// The seller's cost for this product.
    private Integer length;					    // The length of the product. Unit of measurement is UI dependent. No conversions will be made from one UOM to another. See our knowledge base here for more details.
    private Integer width;					    // The width of the product. Unit of measurement is UI dependent. No conversions will be made from one UOM to another. See our knowledge base here for more details.
    private Integer height;					    // The height of the product. Unit of measurement is UI dependent. No conversions will be made from one UOM to another. See our knowledge base here for more details.
    private Integer weightOz;					// The weight of a single item in ounces.
    private String internalNotes;				// Seller's private notes for the product.
    private String fulfillmentSku;				// Stock keeping Unit for the fulfillment of that product by a 3rd party.
    private String createDate;					// The timestamp the product record was created in ShipStation's database. Read-Only.
    private String modifyDate;					// The timestamp the product record was modified in ShipStation. Read-Only.
    private Boolean active;                     // Specifies whether or not the product is an active record.
    private ProductCategory productCategory;	// The Product Category used to organize and report on similar products. See our knowledge base here for more information on Product Categories.
    private String productType;					// Specifies the product type. See our knowledge base here for more information on Product Types.
    private String warehouseLocation;			// The warehouse location associated with the product record.
    private String defaultCarrierCode;			// The default domestic shipping carrier for this product.
    private String defaultServiceCode;			// The default domestic shipping service for this product.
    private String defaultPackageCode;			// The default domestic packageType for this product.
    private String defaultIntlCarrierCode;		// The default international shipping carrier for this product.
    private String defaultIntlServiceCode;		// The default international shipping service for this product.
    private String defaultIntlPackageCode;		// The default international packageType for this product.
    private String defaultConfirmation;			// The default domestic Confirmation type for this Product.
    private String defaultIntlConfirmation;		// The default international Confirmation type for this Product.
    private String customsDescription;			// The default customs Description for the product.
    private Double customsValue;				// The default customs Declared Value for the product.
    private String customsTariffNo;				// The default Harmonized Code for the Product.
    private String customsCountryCode;			// The default 2 digit ISO Origin Country for the Product.
    private Boolean noCustoms;					// If true, this product will not be included on international customs forms.
    private ProductTag tags;					// The Product Tag used to organize and visually identify products. See our knowledge base here for more information on Product Defaults and tags.
}
