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
@Table(name = "schedule_slice")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleSlice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_slice_id")
    private Long id;

    private String name;

    private String contents;

    private LocalDateTime sliceStartDate;

    private LocalDateTime sliceEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
