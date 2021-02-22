package com.schedulsharing.dto.Club;

import lombok.Data;

@Data
public class ClubResponse {
    private Long clubId;
    private String clubName;
    private String categories;
    private Long leaderId;
}
