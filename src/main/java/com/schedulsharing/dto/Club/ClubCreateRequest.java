package com.schedulsharing.dto.Club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubCreateRequest {
    private String clubName;
    private String categories;
}
