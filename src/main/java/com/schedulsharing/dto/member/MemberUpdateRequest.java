package com.schedulsharing.dto.member;

import com.schedulsharing.entity.member.Member;
import com.schedulsharing.entity.member.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    @NotNull
    private String imagePath;
}
