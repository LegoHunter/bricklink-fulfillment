package com.bricklink.fulfillment;

public class BricklinkFulfillmentException extends RuntimeException {
    public BricklinkFulfillmentException() {
    }

    public BricklinkFulfillmentException(String message) {
        super(message);
    }

    public BricklinkFulfillmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public BricklinkFulfillmentException(Throwable cause) {
        super(cause);
    }

    public BricklinkFulfillmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
