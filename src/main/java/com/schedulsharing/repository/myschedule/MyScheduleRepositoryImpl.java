package com.schedulsharing.repository.myschedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.schedulsharing.dto.MySchedule.MyYearMonthRequest;
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
    public List<MySchedule> findAllByEmail(String email, MyYearMonthRequest myYearMonthRequest) {
        System.out.println("email = " + email);
        System.out.println("myYearMonthRequest = " + myYearMonthRequest.getMyYearMonth());
        return queryFactory.selectFrom(mySchedule)
                .where(mySchedule.scheduleStartDate.year().eq(myYearMonthRequest.getMyYearMonth().getYear())
                        .and(mySchedule.scheduleEndDate.year().eq(myYearMonthRequest.getMyYearMonth().getMonthValue()))
                        .or(mySchedule.scheduleEndDate.year().eq(myYearMonthRequest.getMyYearMonth().getYear()))
                        .and(mySchedule.scheduleEndDate.month().eq(myYearMonthRequest.getMyYearMonth().getMonthValue())))
                .fetch();
    }
}
