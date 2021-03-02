package com.schedulsharing.excpetion.common;

public class InvalidGrantException extends RuntimeException{
    public InvalidGrantException() {
        super();
    }

    public InvalidGrantException(String message) {
        super(message);
    }

    public InvalidGrantException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidGrantException(Throwable cause) {
        super(cause);
    }

    protected InvalidGrantException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
