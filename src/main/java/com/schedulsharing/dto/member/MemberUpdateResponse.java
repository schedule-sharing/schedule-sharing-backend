package com.schedulsharing.dto.member;

import lombok.Data;

@Data
public class MemberUpdateResponse {
    private Long id;
    private String name;
    private String email;
    private String imagePath;
}
