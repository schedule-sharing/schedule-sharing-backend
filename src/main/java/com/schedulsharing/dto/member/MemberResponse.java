package com.schedulsharing.dto.member;

import lombok.Data;

@Data
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private String imagePath;
}
