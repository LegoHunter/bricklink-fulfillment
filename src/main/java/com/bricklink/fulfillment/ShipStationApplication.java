package com.bricklink.fulfillment;

import com.bricklink.fulfillment.api.shipstation.*;
import com.bricklink.fulfillment.shipstation.configuration.ShipStationProperties;
import com.bricklink.fulfillment.shipstation.model.*;
import com.bricklink.fulfillment.shipstation.model.Customer.CustomersList;
import com.bricklink.fulfillment.shipstation.model.Package;
import com.bricklink.fulfillment.shipstation.model.Order.OrdersList;
import com.bricklink.fulfillment.shipstation.model.Shipment.ShipmentList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
@SpringBootApplication
public class ShipStationApplication {
    BiConsumer<String, List> listLogger = (s, l) -> {
        log.info("");
        log.info("{} size [{}] --------------------------------------------------", s, l.size());
        l.forEach(o -> log.info("[{}]", o));
    };

    public static void main(String[] args) {
        SpringApplication.run(ShipStationApplication.class, args);
    }

    @Component
    @RequiredArgsConstructor
    private class ShipStationTest implements ApplicationRunner {
        private final ShipStationProperties shipStationProperties;
        private final Carriers carriersService;
        private final Accounts accountsService;
        private final Customers customerService;
        private final Fulfillments fulfillmentService;
        private final Shipments shipmentService;
        private final Orders orderService;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            log.info("ShipStation properties [{}]", shipStationProperties);

            List<Tag> tags = accountsService.listTags();
            listLogger.accept("Tags", tags);

            List<Carrier> carriers = carriersService.getCarriers();
            listLogger.accept("Carriers", carriers);

            Carrier carrier = carriersService.getCarrier("stamps_com");
            log.info("carrier [{}]", carrier);

            List<Package> packages = carriersService.getPackages("stamps_com");
            listLogger.accept("Packages", packages);

            List<Service> services = carriersService.getServices("stamps_com");
            listLogger.accept("Services", services);

            CustomersList customers = customerService.getCustomers(new ParamsBuilder().get());
            listLogger.accept("Customers", customers.getCustomers());

            Customer customer = customerService.getCustomer(5592300L);
            log.info("customer [{}]", customer);

            ShipmentList shipments = shipmentService.getShipments();
            listLogger.accept("Shipments", shipments.getShipments());

            ShipmentList shipmentsWithParameters = shipmentService.getShipments(new ParamsBuilder().of("orderNumber", "BL-11865652")
                                                                                                   .get());
            listLogger.accept("Shipments", shipmentsWithParameters.getShipments());

            OrdersList ordersList = orderService.getOrders();
            listLogger.accept("Orders", ordersList.getOrders());

            OrdersList ordersWithParameters = orderService.getOrders(new ParamsBuilder().of("orderNumber", "BL-11865652").get());
            listLogger.accept("Orders", ordersWithParameters.getOrders());


        }
    }
}
