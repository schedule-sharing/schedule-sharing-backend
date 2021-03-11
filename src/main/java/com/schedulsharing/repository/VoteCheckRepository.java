package com.schedulsharing.repository;

import com.schedulsharing.entity.VoteCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoteCheckRepository extends JpaRepository<VoteCheck, Long> {
    @Query("select vc from VoteCheck vc,ScheduleSuggestion ss,Member m where vc.scheduleSuggestion.id=:suggestionId and vc.member.id=:memberId")
    Optional<VoteCheck> findBySuggestionIdAndMemberId(@Param("suggestionId") Long suggestionId, @Param("memberId") Long memberId);
}
