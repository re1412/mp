package com.re1412.river.hint;

import com.re1412.river.game.Games;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "game_hint")
public class Hint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hint_id")
    private Long hintId;

    @Column(name = "hint")
    private String hint;

}
