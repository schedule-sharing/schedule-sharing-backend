package com.schedulsharing.excpetion.scheduleSuggestion;

public class DuplicateVoteCheckException extends RuntimeException{
    public DuplicateVoteCheckException() {
        super();
    }

    public DuplicateVoteCheckException(String message) {
        super(message);
    }

    public DuplicateVoteCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateVoteCheckException(Throwable cause) {
        super(cause);
    }

    protected DuplicateVoteCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
