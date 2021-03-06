package com.schedulsharing.repository.clubSchedule;

import com.schedulsharing.dto.yearMonth.YearMonthRequest;
import com.schedulsharing.entity.schedule.ClubSchedule;

import java.util.List;

public interface ClubScheduleCustomRepository {
    List<ClubSchedule> findAllByClubId(Long clubId, YearMonthRequest yearMonthRequest);
}
