package com.re1412.river.game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Games, Long> {
    @EntityGraph(attributePaths = {"hint", "users"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT g FROM Games g JOIN g.users u LEFT OUTER JOIN g.hint h WHERE g.active = 1 AND u.userId = :userId")
    List<Games> findByCreator(Long userId);

    @EntityGraph(attributePaths = {"hint", "users", "score"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT g FROM Games g JOIN g.users u LEFT OUTER JOIN g.hint h LEFT OUTER JOIN g.score s WHERE g.active = 1 ORDER BY g.gameId DESC")
    Page<Games> findAllGame(Pageable pageable);
}