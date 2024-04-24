package com.re1412.river.game;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Quiz {
    public Long gameId;
    public Long hintId;
    public String question;
    public String answer;
    public String hint;
    public Long userId;
}
