package com.bricklink.fulfillment.api.shipstation;

import com.bricklink.fulfillment.shipstation.model.Customer;
import com.bricklink.fulfillment.shipstation.model.Customer.CustomersList;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

public interface Customers {
    @RequestLine("GET /customers?stateCode={stateCode}&countryCode={countryCode}&tagId={tagId}&marketplaceId={marketplaceId}&sortBy={sortBy}&sortDir={sortDir}&page={page}&pageSize={pageSize}")
    CustomersList getCustomers(@QueryMap Map<String, Object> params);

    @RequestLine("GET /customers/{customerId}")
    Customer getCustomer(@Param("customerId") Long customerId);
}
