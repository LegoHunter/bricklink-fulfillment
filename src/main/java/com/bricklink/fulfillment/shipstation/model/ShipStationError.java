package com.bricklink.fulfillment.shipstation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShipStationError {
    @JsonProperty("Message")
    private String message;
    @JsonProperty("ExceptionMessage")
    private String exceptionMessage;
    @JsonProperty("ExceptionType")
    private String exceptionType;
    @JsonProperty("StackTrace")
    private String stackTrace;
    private String deserializationErrorMessage;
}
