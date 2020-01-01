package com.bricklink.fulfillment.shipstation.configuration;

import com.bricklink.fulfillment.BricklinkFulfillmentException;
import com.bricklink.fulfillment.api.shipstation.*;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ShipStationProperties.class)
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
    public Accounts accounts(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Accounts.class, shipStationProperties.getShipStation()
                                                             .getBaseUrl());
    }

    @Bean
    public Carriers carriers(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Carriers.class, shipStationProperties.getShipStation()
                                                             .getBaseUrl());
    }

    @Bean
    public Customers customers(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Customers.class, shipStationProperties.getShipStation()
                                                              .getBaseUrl());
    }

    @Bean
    public Fulfillments fulfillments(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Fulfillments.class, shipStationProperties.getShipStation()
                                                                 .getBaseUrl());
    }

    @Bean
    public Shipments shipments(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Shipments.class, shipStationProperties.getShipStation()
                                                                 .getBaseUrl());
    }


    @Bean
    public Orders orders(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Orders.class, shipStationProperties.getShipStation()
                                                              .getBaseUrl());
    }

    @Bean
    public Stores stores(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Stores.class, shipStationProperties.getShipStation()
                                                              .getBaseUrl());
    }

    @Bean
    public Users users(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Users.class, shipStationProperties.getShipStation()
                                                           .getBaseUrl());
    }

    @Bean
    public Warehouses warehouses(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Warehouses.class, shipStationProperties.getShipStation()
                                                          .getBaseUrl());
    }

    @Bean
    public Webhooks webhooks(@Qualifier("shipStationObjectMapper") ObjectMapper shipStationObjectMapper, ShipStationProperties shipStationProperties) {
        return builder(shipStationObjectMapper, shipStationProperties)
                .target(Webhooks.class, shipStationProperties.getShipStation()
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
                    .errorDecoder(new ShipStationErrorDecoder())
                    .requestInterceptor(new BasicAuthRequestInterceptor(username, credential))
                    .logger(new Slf4jLogger(Accounts.class))
                    .logLevel(feign.Logger.Level.FULL);

        }
        return builder;
    }

    private class ShipStationErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder _default = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() >= 400 && response.status() <= 499) {
                throw new BricklinkFulfillmentException(String.format("%d - %s", response.status(), response.reason()));
            }
            if (response.status() >= 500 && response.status() <= 599) {
                throw new BricklinkFulfillmentException(String.format("%d - %s", response.status(), response.reason()));
            }
            return _default.decode(methodKey, response);
        }
    }
}
