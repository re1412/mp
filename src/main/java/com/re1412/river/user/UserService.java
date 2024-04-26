package com.re1412.river.user;

import com.re1412.river.game.GameRepository;
import com.re1412.river.game.Games;
import com.re1412.river.score.Score;
import com.re1412.river.score.ScoreRepository;
import com.re1412.river.score.UserScore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    public final GameRepository gameRepository;
    public final ScoreRepository scoreRepository;
    public final UserRepository userRepository;

    public UserInfo userInfo(Long userId) {
        UserInfo userInfo = new UserInfo();
        // 게임 등록 개수
        List<Games> games = gameRepository.findByCreator(userId);
        userInfo.setWritten(games.size());
        // 게임 점수
        Integer sumPoint = scoreRepository.sumScorePointByUserId(userId);
        if( sumPoint == null ) {
            userInfo.setPoint(0);
        } else {
            userInfo.setPoint(sumPoint);
        }
        return userInfo;
    }

    public Users findUser(Long userId){
        Optional<Users> optionalGames = userRepository.findById(userId);
        if (optionalGames.isPresent()) {
            return optionalGames.get();
        }
        throw new EntityNotFoundException("User not found with id: " + userId);
    }

    public List<UserScore> userScoreList() {
        List<Object[]> results = scoreRepository.userScoreList();
        List<UserScore> userScores = new ArrayList<>();
        for (Object[] result : results) {
            Users users = (Users) result[0];
            Long totalScore = (Long) result[1];
            userScores.add(new UserScore(users, totalScore));
        }
        return userScores;
    }
}
