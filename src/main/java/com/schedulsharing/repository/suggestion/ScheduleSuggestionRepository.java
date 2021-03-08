package com.schedulsharing.repository.suggestion;

import com.schedulsharing.dto.suggestion.SuggestionListRequest;
import com.schedulsharing.entity.schedule.ScheduleSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleSuggestionRepository extends JpaRepository<ScheduleSuggestion, Long> ,ScheduleSuggestionCustomRepository{

}
