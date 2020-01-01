package com.bricklink.fulfillment.api.shipstation;

import com.bricklink.fulfillment.shipstation.model.Package;
import com.bricklink.fulfillment.shipstation.model.Carrier;
import com.bricklink.fulfillment.shipstation.model.Service;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface Carriers {
    @RequestLine("GET /carriers")
    List<Carrier> getCarriers();

    @RequestLine("GET /carriers/getcarrier?carrierCode={carrierCode}")
    Carrier getCarrier(@Param("carrierCode") String carrierCode);

    @RequestLine("GET /carriers/listpackages?carrierCode={carrierCode}")
    List<Package> getPackages(@Param("carrierCode") String carrierCode);

    @RequestLine("GET /carriers/listservices?carrierCode={carrierCode}")
    List<Service> getServices(@Param("carrierCode") String carrierCode);
}
