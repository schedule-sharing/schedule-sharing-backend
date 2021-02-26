package com.schedulsharing.entity.schedule;

import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "club_schedule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_schedule_id")
    private Long id;

    private String name;

    private String contents;

    private LocalDateTime startMeetingDate;

    private LocalDateTime endMeetingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
