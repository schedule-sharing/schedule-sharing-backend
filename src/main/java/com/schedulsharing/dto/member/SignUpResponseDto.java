package com.schedulsharing.dto.member;

import lombok.Data;

@Data
public class SignUpResponseDto {
    private Long id;
    private String email;
    private String name;
    private String imagePath;
}
