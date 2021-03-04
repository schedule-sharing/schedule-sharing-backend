package com.schedulsharing.repository.clubSchedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.schedulsharing.dto.ClubSchedule.YearMonthRequest;
import com.schedulsharing.entity.schedule.ClubSchedule;
import com.schedulsharing.entity.schedule.QClubSchedule;

import javax.persistence.EntityManager;
import java.util.List;

import static com.schedulsharing.entity.schedule.QClubSchedule.*;

public class ClubScheduleRepositoryImpl implements ClubScheduleCustomRepository {
    private JPAQueryFactory queryFactory;

    public ClubScheduleRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ClubSchedule> findAllByClubId(Long clubId, YearMonthRequest yearMonthRequest) {
        return queryFactory.selectFrom(clubSchedule)
                .where(clubSchedule.startMeetingDate.year().eq(yearMonthRequest.getYearMonth().getYear())
                        .and(clubSchedule.endMeetingDate.year().eq(yearMonthRequest.getYearMonth().getMonthValue()))
                        .or(clubSchedule.endMeetingDate.year().eq(yearMonthRequest.getYearMonth().getYear()))
                        .and(clubSchedule.endMeetingDate.month().eq(yearMonthRequest.getYearMonth().getMonthValue())))
                .fetch();
    }
}
