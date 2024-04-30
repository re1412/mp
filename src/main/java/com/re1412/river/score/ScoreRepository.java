package com.re1412.river.score;

import com.re1412.river.user.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    @Query("SELECT SUM(s.scorePoint) FROM Score s WHERE s.users.userId = :userId")
    Integer sumScorePointByUserId(@Param("userId") Long userId);

    @Query("SELECT s.users, SUM(s.scorePoint) AS totalScore FROM Score s GROUP BY s.users ORDER BY totalScore DESC")
    List<Object[]> userScoreList();

    @Query("SELECT s.users, SUM(s.scorePoint) AS totalScore FROM Score s WHERE DATE(s.createdDate) = CURDATE() GROUP BY s.users ORDER BY totalScore DESC")
    List<Object[]> userScoreTodayList();
}