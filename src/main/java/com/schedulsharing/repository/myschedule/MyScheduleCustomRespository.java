package com.schedulsharing.repository.myschedule;

import com.schedulsharing.dto.MySchedule.MyYearMonthRequest;
import com.schedulsharing.entity.schedule.MySchedule;

import java.util.List;

public interface MyScheduleCustomRespository {
    List<MySchedule> findAllByEmail(String email, MyYearMonthRequest myYearMonthRequest);
}
