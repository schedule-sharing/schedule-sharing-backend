package com.schedulsharing.repository;

import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String username);

    @Query("select c from Member m ,Club c, MemberClub mc " +
            "where m.id=mc.member.id and c.id=mc.club.id and m.email=:email")
    List<Club> findAllClub(@Param("email") String email);
}
