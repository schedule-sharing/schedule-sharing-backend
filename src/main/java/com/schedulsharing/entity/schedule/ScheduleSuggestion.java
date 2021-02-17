package com.schedulsharing.entity.schedule;

import com.schedulsharing.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "schedule_suggestion")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_suggestion_id")
    private Long id;

    private String title;

    private String contents;

    private String location;

    private int minMember;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus scheduleStatus;

    private LocalDateTime meetingDate;

    private LocalDateTime startVoteDate;

    private LocalDateTime endVoteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
