package com.schedulsharing.dto.MySchedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyScheduleCreateResponse {
    private Long myScheduleId;
    private String name;
    private String contents;
    private LocalDateTime scheduleStartDate;
    private LocalDateTime scheduleEndDate;
}
