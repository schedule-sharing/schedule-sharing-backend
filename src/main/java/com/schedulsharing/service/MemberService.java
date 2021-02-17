package com.schedulsharing.service;


import com.schedulsharing.dto.MemberDto;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.entity.member.MemberRole;
import com.schedulsharing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signup(MemberDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        List<MemberRole> role = new ArrayList<>();
        role.add(MemberRole.USER);

        Member user = Member.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .roles(role)
                .build();


        return userRepository.save(user);
    }

}
