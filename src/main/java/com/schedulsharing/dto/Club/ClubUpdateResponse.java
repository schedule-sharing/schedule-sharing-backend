package com.schedulsharing.dto.Club;

import lombok.Data;

@Data
public class ClubUpdateResponse {
    private Long clubId;
    private String clubName;
    private String categories;
    private Long leaderId;
}
