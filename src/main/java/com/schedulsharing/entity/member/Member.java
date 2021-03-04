package com.schedulsharing.entity.member;

import com.schedulsharing.dto.member.MemberUpdateRequest;
import com.schedulsharing.entity.MemberClub;
import com.schedulsharing.entity.schedule.ClubSchedule;
import com.schedulsharing.entity.schedule.MySchedule;
import io.jsonwebtoken.Claims;
import lombok.*;

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

    public void update(MemberUpdateRequest memberUpdateRequest) {
        this.name = memberUpdateRequest.getName();
        this.password = memberUpdateRequest.getPassword();
        this.imagePath = memberUpdateRequest.getImagePath();
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<MemberClub> memberClubs = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ClubSchedule> clubSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<MySchedule> mySchedules = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<MemberRole> roles;

}