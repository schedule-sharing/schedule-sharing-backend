package com.schedulsharing.dto.member;

import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "clubList")
public class GetClubsResponse {
    private Long clubId;
    private String clubName;
    private String categories;
    private Long leaderId;
}
