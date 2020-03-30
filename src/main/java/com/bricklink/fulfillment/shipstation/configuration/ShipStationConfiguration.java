package com.bricklink.fulfillment.shipstation.configuration;

import com.bricklink.fulfillment.ShipStationException;
import com.bricklink.fulfillment.api.ReferenceService;
import com.bricklink.fulfillment.api.bricklink.ReferenceServiceImpl;
import com.bricklink.fulfillment.api.shipstation.AccountsAPI;
import com.bricklink.fulfillment.api.shipstation.CarriersAPI;
import com.bricklink.fulfillment.api.shipstation.CustomersAPI;
import com.bricklink.fulfillment.api.shipstation.FulfillmentsAPI;
import com.bricklink.fulfillment.api.shipstation.OrdersAPI;
import com.bricklink.fulfillment.api.shipstation.ShipStationOrderService;
import com.bricklink.fulfillment.api.shipstation.ShipmentsAPI;
import com.bricklink.fulfillment.api.shipstation.StoresAPI;
import com.bricklink.fulfillment.api.shipstation.UsersAPI;
import com.bricklink.fulfillment.api.shipstation.WarehousesAPI;
import com.bricklink.fulfillment.api.shipstation.WebhooksAPI;
import com.bricklink.fulfillment.shipstation.model.ShipStationError;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.Response;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties({ReferenceProperties.class, ShipStationProperties.class})
public class ShipStationConfiguration {

    private Feign.Builder builder;

    @Bean
    @Qualifier("shipStationObjectMapper")
    public ObjectMapper shipStationObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Bean
    public ShipStationOrderService shipStationOrderService(OrdersAPI ordersApi, ShipmentsAPI shipmentsApi) {
        return new ShipStationOrderService(ordersApi, shipmentsApi);
    }

    @Bean
    public ReferenceService referenceService(ReferenceProperties referenceProperties, ObjectMapper objectMapper) {
        return new ReferenceServiceImpl(referenceProperties, objectMapper);
    }

    @Bean
    public AccountsAPI accounts(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(AccountsAPI.class, shipStationProperties.getShipStation()
                                                                .getBaseUrl());
    }

    @Bean
    public CarriersAPI carriers(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(CarriersAPI.class, shipStationProperties.getShipStation()
                                                                .getBaseUrl());
    }

    @Bean
    public CustomersAPI customers(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(CustomersAPI.class, shipStationProperties.getShipStation()
                                                                 .getBaseUrl());
    }

    @Bean
    public FulfillmentsAPI fulfillments(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(FulfillmentsAPI.class, shipStationProperties.getShipStation()
                                                                    .getBaseUrl());
    }

    @Bean
    public ShipmentsAPI shipments(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(ShipmentsAPI.class, shipStationProperties.getShipStation()
                                                                 .getBaseUrl());
    }


    @Bean
    public OrdersAPI orders(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(OrdersAPI.class, shipStationProperties.getShipStation()
                                                              .getBaseUrl());
    }

    @Bean
    public StoresAPI stores(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(StoresAPI.class, shipStationProperties.getShipStation()
                                                              .getBaseUrl());
    }

    @Bean
    public UsersAPI users(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(UsersAPI.class, shipStationProperties.getShipStation()
                                                             .getBaseUrl());
    }

    @Bean
    public WarehousesAPI warehouses(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(WarehousesAPI.class, shipStationProperties.getShipStation()
                                                                  .getBaseUrl());
    }

    @Bean
    public WebhooksAPI webhooks(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(WebhooksAPI.class, shipStationProperties.getShipStation()
                                                                .getBaseUrl());
    }

    private Feign.Builder builder(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        if (null == builder) {
            String username = shipStationProperties.getShipStation()
                                                   .getSecrets()
                                                   .getApiKey();
            String credential = shipStationProperties.getShipStation()
                                                     .getSecrets()
                                                     .getApiSecret();
            builder = Feign
                    .builder()
                    .client(new OkHttpClient())
                    .encoder(new JacksonEncoder(shipStationObjectMapper))
                    .decoder(new JacksonDecoder(shipStationObjectMapper))
                    .errorDecoder(new ShipStationErrorDecoder(shipStationObjectMapper))
                    .requestInterceptor(new BasicAuthRequestInterceptor(username, credential))
                    .logger(new Slf4jLogger(AccountsAPI.class))
                    .logLevel(feign.Logger.Level.FULL);

        }
        return builder;
    }

    @RequiredArgsConstructor
    @Slf4j
    private static class ShipStationErrorDecoder implements ErrorDecoder {
        private final ObjectMapper shipStationObjectMapper;

        @Override
        public Exception decode(String methodKey, Response response) {
            final ShipStationError shipStationError =
                    Optional.ofNullable(response.body())
                            .map(b -> {
                                try {
                                    return shipStationObjectMapper.readValue(b.asInputStream(), ShipStationError.class);
                                } catch (IOException e) {
                                    ShipStationError ssError = new ShipStationError();
                                    ssError.setDeserializationErrorMessage(e.getMessage());
                                    return ssError;
                                }
                            })
                            .get();
            log.error("ShipStation Error [{}]", shipStationError);
            return new ShipStationException(shipStationError, String.format("%d - %s", response.status(), response.reason()));
        }
    }
}
