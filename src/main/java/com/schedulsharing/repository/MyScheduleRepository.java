package com.schedulsharing.repository;

import com.schedulsharing.entity.schedule.MySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyScheduleRepository extends JpaRepository<MySchedule, Long> {
}
