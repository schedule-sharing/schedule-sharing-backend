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
@Table(name = "my_schedule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MySchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_schedule_id")
    private Long id;

    private String name;

    private String contents;

    private LocalDateTime scheduleStartDate;

    private LocalDateTime scheduleEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
