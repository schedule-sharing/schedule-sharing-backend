package com.schedulsharing.repository.suggestion;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.schedulsharing.dto.yearMonth.YearMonthRequest;
import com.schedulsharing.entity.schedule.ScheduleSuggestion;

import javax.persistence.EntityManager;
import java.util.List;

import static com.schedulsharing.entity.schedule.QScheduleSuggestion.scheduleSuggestion;

public class ScheduleSuggestionRepositoryImpl implements ScheduleSuggestionCustomRepository {
    private JPAQueryFactory queryFactory;

    public ScheduleSuggestionRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ScheduleSuggestion> findAllByClubIdConfirm(Long clubId, YearMonthRequest yearMonthRequest) {
        return queryFactory.selectFrom(scheduleSuggestion)
                .where(scheduleSuggestion.scheduleStartDate.year().eq(yearMonthRequest.getYearMonth().getYear())
                        .and(scheduleSuggestion.scheduleStartDate.month().eq(yearMonthRequest.getYearMonth().getMonthValue()))
                        .or(scheduleSuggestion.scheduleEndDate.year().eq(yearMonthRequest.getYearMonth().getYear())
                        .and(scheduleSuggestion.scheduleEndDate.month().eq(yearMonthRequest.getYearMonth().getMonthValue())))
                        .and(scheduleSuggestion.isConfirm.eq(true)))
                .fetch();
    }
}
