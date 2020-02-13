package com.bricklink.fulfillment;

import com.bricklink.api.rest.client.BricklinkRestClient;
import com.bricklink.fulfillment.api.shipstation.AccountsAPI;
import com.bricklink.fulfillment.api.shipstation.CarriersAPI;
import com.bricklink.fulfillment.api.shipstation.CustomersAPI;
import com.bricklink.fulfillment.api.shipstation.FulfillmentsAPI;
import com.bricklink.fulfillment.api.shipstation.OrdersAPI;
import com.bricklink.fulfillment.api.shipstation.ParamsBuilder;
import com.bricklink.fulfillment.api.shipstation.ShipmentsAPI;
import com.bricklink.fulfillment.shipstation.configuration.ShipStationProperties;
import com.bricklink.fulfillment.shipstation.model.Carrier;
import com.bricklink.fulfillment.shipstation.model.Customer;
import com.bricklink.fulfillment.shipstation.model.Customer.CustomersList;
import com.bricklink.fulfillment.shipstation.model.Package;
import com.bricklink.fulfillment.shipstation.model.Service;
import com.bricklink.fulfillment.shipstation.model.Shipment.ShipmentList;
import com.bricklink.fulfillment.shipstation.model.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiConsumer;

import static com.bricklink.fulfillment.shipstation.model.ShipStationOrder.OrdersList;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com", "net.bricklink"})
public class ShipStationApplication {
    BiConsumer<String, List> listLogger = (s, l) -> {
        log.info("");
        log.info("{} size [{}] --------------------------------------------------", s, l.size());
        l.forEach(o -> log.info("[{}]", o));
    };

    public static void main(String[] args) {
        SpringApplication.run(ShipStationApplication.class, args);
    }

    //@Component
    @RequiredArgsConstructor
    private class ShipStationTest implements ApplicationRunner {
        private final ShipStationProperties shipStationProperties;
        private final BricklinkRestClient bricklinkRestClient;
        private final CarriersAPI carriersAPIService;
        private final AccountsAPI accountsAPIService;
        private final CustomersAPI customerService;
        private final FulfillmentsAPI fulfillmentService;
        private final ShipmentsAPI shipmentService;
        private final OrdersAPI orderService;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            log.info("ShipStation properties [{}]", shipStationProperties);

            List<Tag> tags = accountsAPIService.listTags();
            listLogger.accept("Tags", tags);

            List<Carrier> carriers = carriersAPIService.getCarriers();
            listLogger.accept("Carriers", carriers);

            Carrier carrier = carriersAPIService.getCarrier("stamps_com");
            log.info("carrier [{}]", carrier);

            List<Package> packages = carriersAPIService.getPackages("stamps_com");
            listLogger.accept("Packages", packages);

            List<Service> services = carriersAPIService.getServices("stamps_com");
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
