package com.schedulsharing.repository.suggestion;

import com.schedulsharing.dto.yearMonth.YearMonthRequest;
import com.schedulsharing.entity.schedule.ScheduleSuggestion;

import java.util.List;

public interface ScheduleSuggestionCustomRepository {
    List<ScheduleSuggestion> findAllByClubIdConfirm(Long clubId, YearMonthRequest yearMonthRequest);
}
