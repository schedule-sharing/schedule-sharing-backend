package com.schedulsharing.excpetion.club;

public class ClubNotFoundException extends RuntimeException{
    public ClubNotFoundException() {
        super();
    }

    public ClubNotFoundException(String message) {
        super(message);
    }

    public ClubNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClubNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ClubNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
