package com.schedulsharing.repository.clubSchedule;

import com.schedulsharing.entity.schedule.ClubSchedule;

import java.time.YearMonth;
import java.util.List;

public interface ClubScheduleCustomRepository {
    List<ClubSchedule> findAllByClubId(Long clubId, YearMonth yearMonth);
}
