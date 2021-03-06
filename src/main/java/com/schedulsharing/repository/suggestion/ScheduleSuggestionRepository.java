package com.schedulsharing.repository.suggestion;

import com.schedulsharing.entity.schedule.ScheduleSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleSuggestionRepository extends JpaRepository<ScheduleSuggestion, Long> ,ScheduleSuggestionCustomRepository{
}
