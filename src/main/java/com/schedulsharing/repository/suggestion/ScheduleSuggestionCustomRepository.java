package com.schedulsharing.repository.suggestion;

import com.schedulsharing.entity.schedule.ScheduleSuggestion;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ScheduleSuggestionCustomRepository {

    List<ScheduleSuggestion> findAllByClubIdConfirm(Long clubId, YearMonth yearMonth);

    List<ScheduleSuggestion> findAllByClubId(Long clubId, LocalDate now);
}
