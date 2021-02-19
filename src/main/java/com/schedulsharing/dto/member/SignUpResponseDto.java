package com.schedulsharing.dto.member;

import lombok.Data;

@Data
public class SignUpResponseDto {
    private String email;
    private String name;
    private String imagePath;
}
