package com.re1412.river.score;

import com.re1412.river.user.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserScore {
    public Users users;
    public Long totalScore;

    public UserScore(Users users, Long totalScore) {
        this.users = users;
        this.totalScore = totalScore;
    }
}
