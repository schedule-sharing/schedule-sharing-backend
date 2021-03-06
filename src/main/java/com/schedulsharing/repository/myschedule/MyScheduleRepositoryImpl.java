package com.schedulsharing.repository.myschedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.schedulsharing.dto.yearMonth.YearMonthRequest;
import com.schedulsharing.entity.schedule.MySchedule;

import javax.persistence.EntityManager;
import java.util.List;

import static com.schedulsharing.entity.schedule.QMySchedule.mySchedule;

public class MyScheduleRepositoryImpl implements MyScheduleCustomRespository {

    private JPAQueryFactory queryFactory;

    public MyScheduleRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<MySchedule> findAllByEmail(String email, YearMonthRequest yearMonthRequest) {
        return queryFactory.selectFrom(mySchedule)
                .where(mySchedule.scheduleStartDate.year().eq(yearMonthRequest.getYearMonth().getYear())
                        .and(mySchedule.scheduleStartDate.month().eq(yearMonthRequest.getYearMonth().getMonthValue()))
                        .or(mySchedule.scheduleEndDate.year().eq(yearMonthRequest.getYearMonth().getYear()))
                        .and(mySchedule.scheduleEndDate.month().eq(yearMonthRequest.getYearMonth().getMonthValue()))
                        .and(mySchedule.member.email.eq(email)))
                .fetch();
    }
}
