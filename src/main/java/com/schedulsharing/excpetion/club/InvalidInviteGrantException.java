package com.schedulsharing.excpetion.club;

public class InvalidInviteGrantException extends RuntimeException{
    public InvalidInviteGrantException() {
        super();
    }

    public InvalidInviteGrantException(String message) {
        super(message);
    }

    public InvalidInviteGrantException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInviteGrantException(Throwable cause) {
        super(cause);
    }

    protected InvalidInviteGrantException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
