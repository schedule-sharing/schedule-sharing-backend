package com.schedulsharing.excpetion.clubSchedule;

public class ClubScheduleNotFoundException extends RuntimeException{
    public ClubScheduleNotFoundException() {
        super();
    }

    public ClubScheduleNotFoundException(String message) {
        super(message);
    }

    public ClubScheduleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClubScheduleNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ClubScheduleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
