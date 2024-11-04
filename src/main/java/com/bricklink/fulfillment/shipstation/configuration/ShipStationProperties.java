package com.bricklink.fulfillment.shipstation.configuration;

import com.bricklink.fulfillment.BricklinkFulfillmentException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Setter
@Getter
@ToString
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "shipstation")
public class ShipStationProperties {
    private Path clientConfigDir;
    private Path clientConfigFile;
    private ShipStation shipStation;

    public void setClientConfigDir(Path clientConfigDir) {
        this.clientConfigDir = clientConfigDir;
        loadPropertiesFromJson();
    }

    public void setClientConfigFile(Path clientConfigFile) {
        this.clientConfigFile = clientConfigFile;
        loadPropertiesFromJson();
    }

    private void loadPropertiesFromJson() {
        Optional<Path> optionalDir = Optional.ofNullable(getClientConfigDir());
        Optional<Path> optionalFile = Optional.ofNullable(getClientConfigFile());
        if ((optionalDir.isPresent()) && (optionalFile.isPresent())) {
            Path jsonConfigFile = Path.of(clientConfigDir.toString(), clientConfigFile.toString());
            if (Files.exists(jsonConfigFile)) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
                mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
                try {
                    shipStation = mapper.readValue(jsonConfigFile.toFile(), ShipStation.class);
                    log.info("Loaded secure configuration [{}] from path [{}]", clientConfigFile, clientConfigDir);
                } catch (IOException e) {
                    throw new BricklinkFulfillmentException(e);
                }
            } else {
                throw new BricklinkFulfillmentException("[" + jsonConfigFile.toAbsolutePath() + "] does not exist");
            }
        }
    }

    @Data
    @JsonRootName(value = "shipstation")
    public static class ShipStation {
        private Secrets secrets;
        @JsonProperty("base-url")
        private String baseUrl;
    }

    @Data
    public static class Secrets {
        @JsonProperty("api-key")
        private String apiKey;

        @JsonProperty("api-secret")
        private String apiSecret;
    }
}
