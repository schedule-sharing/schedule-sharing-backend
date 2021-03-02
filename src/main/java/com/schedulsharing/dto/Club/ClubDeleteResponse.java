package com.schedulsharing.dto.Club;

import lombok.Data;

@Data
public class ClubDeleteResponse {

    private boolean success;
    private String message;

    public ClubDeleteResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
