package com.bricklink.fulfillment.shipstation.model;

import com.bricklink.util.DateUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.net.URL;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tracking {
    private String trackingNumber;
    private URL trackingURL;
    @JsonDeserialize(using = DateUtils.ZonedDateTimeDeserializer.class)
    private ZonedDateTime dateShipped;
}
