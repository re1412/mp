package com.re1412.river.game;

import com.re1412.river.hint.Hint;
import com.re1412.river.score.Score;
import com.re1412.river.user.Users;
import groovy.lang.Lazy;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "games")
public class Games {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "description")
    private String description;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "encrypted_answer")
    private String encryptedAnswer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hint_id")
    private Hint hint;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user_id")
    private Users users;

    @OneToMany(mappedBy = "games", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Score> score = new ArrayList<>();

    @Column(name = "solver_user_id")
    private Long solver;

    @Column(name = "active")
    private Boolean active;
}
