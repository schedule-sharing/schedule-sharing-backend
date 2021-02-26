package com.schedulsharing.entity;

import com.schedulsharing.entity.schedule.ScheduleSuggestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "vote")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    private int agree;

    private int disAgree;

    private boolean isConfirm;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_suggestion_id")
    private ScheduleSuggestion scheduleSuggestion;
}
