package com.schedulsharing.excpetion;

public class MyScheduleNotFoundException extends RuntimeException {
    public MyScheduleNotFoundException() {
        super();
    }

    public MyScheduleNotFoundException(String message) {
        super(message);
    }

    public MyScheduleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyScheduleNotFoundException(Throwable cause) {
        super(cause);
    }

    protected MyScheduleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
