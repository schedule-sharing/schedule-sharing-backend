package com.schedulsharing.entity.member;

import com.schedulsharing.dto.member.MemberUpdateRequest;
import com.schedulsharing.entity.MemberClub;
import com.schedulsharing.entity.schedule.ClubSchedule;
import com.schedulsharing.entity.schedule.MySchedule;
import com.schedulsharing.entity.schedule.ScheduleSuggestion;
import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
@EqualsAndHashCode(of = "id")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    private String imagePath;

    public void update(MemberUpdateRequest memberUpdateRequest, PasswordEncoder passwordEncoder) {
        this.name = memberUpdateRequest.getName();
        this.password = passwordEncoder.encode(memberUpdateRequest.getPassword());
        this.imagePath = memberUpdateRequest.getImagePath();
    }

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<MemberClub> memberClubs = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ClubSchedule> clubSchedules = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<MySchedule> mySchedules = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ScheduleSuggestion> suggestions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<MemberRole> roles;

}