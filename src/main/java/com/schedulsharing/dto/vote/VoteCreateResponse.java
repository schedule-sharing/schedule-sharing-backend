package com.schedulsharing.dto.vote;

import lombok.Data;

@Data
public class VoteCreateResponse {
    private Long id;
    private int agree;
    private int disagree;
    private boolean isConfirm;
}
