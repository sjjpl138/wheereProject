package com.d138.wheere.exception;

public class NotEnoughSeatsException extends RuntimeException {
    public NotEnoughSeatsException() {
        super();
    }

    public NotEnoughSeatsException(String message) {
        super(message);
    }

    public NotEnoughSeatsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughSeatsException(Throwable cause) {
        super(cause);
    }

    public NotEnoughSeatsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
