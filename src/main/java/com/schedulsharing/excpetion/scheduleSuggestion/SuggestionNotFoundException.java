package com.schedulsharing.excpetion.scheduleSuggestion;

public class SuggestionNotFoundException extends RuntimeException{
    public SuggestionNotFoundException() {
        super();
    }

    public SuggestionNotFoundException(String message) {
        super(message);
    }

    public SuggestionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SuggestionNotFoundException(Throwable cause) {
        super(cause);
    }

    protected SuggestionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
