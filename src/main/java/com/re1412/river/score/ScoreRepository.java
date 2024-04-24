package com.re1412.river.score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    @Query("SELECT SUM(s.scorePoint) FROM Score s WHERE s.users.userId = :userId")
    Integer sumScorePointByUserId(@Param("userId") Long userId);
}