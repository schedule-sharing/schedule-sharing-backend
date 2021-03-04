package com.schedulsharing.excpetion.member;

public class EmailExistedException extends RuntimeException{
    public EmailExistedException() {
        super();
    }

    public EmailExistedException(String message) {
        super(message);
    }

    public EmailExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailExistedException(Throwable cause) {
        super(cause);
    }

    protected EmailExistedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
