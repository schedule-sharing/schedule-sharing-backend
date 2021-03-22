package com.schedulsharing.excpetion.vote;

public class VoteNotFoundException extends RuntimeException{
    public VoteNotFoundException() {
    }

    public VoteNotFoundException(String message) {
        super(message);
    }

    public VoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VoteNotFoundException(Throwable cause) {
        super(cause);
    }

    protected VoteNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
