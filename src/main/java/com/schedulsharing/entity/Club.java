package com.schedulsharing.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "club")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    private String clubName;

    private Long leaderId;

    private String categories;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberClub> memberClubs=new ArrayList<>();

    public void addMemberClub(MemberClub memberClub) {
        memberClubs.add(memberClub);
        memberClub.setClub(this);
    }

    public static Club createClub(String clubName, Long leaderId, String categories, MemberClub memberClub) {
        Club club = Club.builder()
                .clubName(clubName)
                .leaderId(leaderId)
                .categories(categories)
                .build();

        club.addMemberClub(memberClub);

        return club;
    }
}
