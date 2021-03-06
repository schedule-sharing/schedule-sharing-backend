package com.schedulsharing.repository.clubSchedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.schedulsharing.dto.yearMonth.YearMonthRequest;
import com.schedulsharing.entity.schedule.ClubSchedule;

import javax.persistence.EntityManager;
import java.util.List;

import static com.schedulsharing.entity.schedule.QClubSchedule.clubSchedule;

public class ClubScheduleRepositoryImpl implements ClubScheduleCustomRepository {
    private JPAQueryFactory queryFactory;

    public ClubScheduleRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ClubSchedule> findAllByClubId(Long clubId, YearMonthRequest yearMonthRequest) {
        return queryFactory.selectFrom(clubSchedule)
                .where(clubSchedule.startMeetingDate.year().eq(yearMonthRequest.getYearMonth().getYear())
                        .and(clubSchedule.startMeetingDate.month().eq(yearMonthRequest.getYearMonth().getMonthValue()))
                        .or(clubSchedule.endMeetingDate.year().eq(yearMonthRequest.getYearMonth().getYear()))
                        .and(clubSchedule.endMeetingDate.month().eq(yearMonthRequest.getYearMonth().getMonthValue()))
                        .and(clubSchedule.club.id.eq(clubId)))
                .fetch();
    }
}
