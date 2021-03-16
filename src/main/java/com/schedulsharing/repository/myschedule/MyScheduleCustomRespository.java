package com.schedulsharing.repository.myschedule;

import com.schedulsharing.entity.schedule.MySchedule;

import java.time.YearMonth;
import java.util.List;

public interface MyScheduleCustomRespository {
    List<MySchedule> findAllByEmail(String email, YearMonth yearMonth);
}
