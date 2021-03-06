package com.schedulsharing.repository.clubSchedule;

import com.schedulsharing.entity.schedule.ClubSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubScheduleRepository extends JpaRepository<ClubSchedule, Long> ,ClubScheduleCustomRepository{

}
