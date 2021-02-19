package com.schedulsharing.dto.member;

import lombok.Data;

@Data
public class EmailCheckResponseDto {
    private boolean duplicate;
    private String message;

    public EmailCheckResponseDto(boolean duplicate, String message) {
        this.duplicate = duplicate;
        this.message = message;
    }

}
