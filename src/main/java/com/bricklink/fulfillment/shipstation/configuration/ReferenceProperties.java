package com.bricklink.fulfillment.shipstation.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@ToString
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "reference")
public class ReferenceProperties {
    private StaticReference staticReference;

    @Data
    public static class StaticReference {
        private String country;
    }
}
