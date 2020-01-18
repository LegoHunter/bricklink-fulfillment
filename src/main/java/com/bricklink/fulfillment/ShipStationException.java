package com.bricklink.fulfillment;

import com.bricklink.fulfillment.shipstation.model.ShipStationError;

public class ShipStationException extends RuntimeException {
    private ShipStationError shipStationError;

    public ShipStationException(ShipStationError shipStationError) {
        super();
        this.shipStationError = shipStationError;
    }

    public ShipStationException(ShipStationError shipStationError, String message) {
        super(message);
        this.shipStationError = shipStationError;
    }

    public ShipStationException(ShipStationError shipStationError, String message, Throwable cause) {
        super(message, cause);
        this.shipStationError = shipStationError;
    }

    public ShipStationException(ShipStationError shipStationError, Throwable cause) {
        super(cause);
        this.shipStationError = shipStationError;
    }

    public ShipStationException(ShipStationError shipStationError, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.shipStationError = shipStationError;
    }
}
