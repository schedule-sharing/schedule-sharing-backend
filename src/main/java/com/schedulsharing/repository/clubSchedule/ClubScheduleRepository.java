package com.schedulsharing.repository.clubSchedule;

import com.schedulsharing.dto.ClubSchedule.YearMonthRequest;
import com.schedulsharing.entity.schedule.ClubSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubScheduleRepository extends JpaRepository<ClubSchedule, Long> ,ClubScheduleCustomRepository{
//    List<ClubSchedule> findAllByClubId(Long clubId, YearMonthRequest yearMonthRequest);
}
