package com.re1412.river.game;

import com.re1412.river.hint.Hint;
import com.re1412.river.hint.HintRepository;
import com.re1412.river.score.Score;
import com.re1412.river.score.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final GameRepository gameRepository;
    private final HintRepository hintRepository;
    private final ScoreRepository scoreRepository;

    public Games gameSave(Games games){
        return gameRepository.save(games);
    }

    public Long quizInsert(Games games) {
        Games savedGame = gameSave(games);
        Long gameId = savedGame.getGameId();
        return gameId;
    }

    public List<Games> gameList(Long userId){
        List<Games> gameList = gameRepository.findByCreator(userId);
        return gameList;
    }

    public Page<Games> gameListAll(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Games> games = gameRepository.findAllGame(pageable);
        return games;
    }
    public Games findGame(Quiz quiz){
        Optional<Games> optionalGames = gameRepository.findById(quiz.getGameId());
        Games existingGames = new Games();
        if ( optionalGames.isPresent() ) {
            existingGames = optionalGames.get();
            // 업데이트된 값을 설정
            if( !quiz.getAnswer().equals(existingGames.getAnswer()) ){
                throw new EntityNotFoundException("incorrect answer");
            }
        } else {
            throw new EntityNotFoundException("User not found with id: " + quiz.getGameId());
        }
        return existingGames;
    }

    public Long hintInsert(Hint hint) {
        Hint savedHint = hintRepository.save(hint);
        Long hintId = savedHint.getHintId();
        return hintId;
    }

    public Long scoreInsert(Score score) {
        Score savedScore = scoreRepository.save(score);
        Long scoreId = savedScore.getScoreId();
        return scoreId;
    }

    public Map<String, String> quizUpdate(Games games, Map<String, String> resultMap) {
        try {
            Optional<Games> optionalGames = gameRepository.findById(games.getGameId());
            if (optionalGames.isPresent()) {
                Games existingGames = optionalGames.get();
                // 업데이트된 값을 설정
                existingGames.setQuestion(games.getQuestion());
                existingGames.setAnswer(games.getAnswer());
                existingGames.setEncryptedAnswer(games.getEncryptedAnswer());
                existingGames.setHint(games.getHint());

                // 업데이트된 Games 객체 저장
                gameRepository.save(existingGames);
                resultMap.put("resultCode", "1");
                resultMap.put("resultHintId", String.valueOf(games.getHint().getHintId()));
                resultMap.put("resultMsg", "퀴즈가 업데이트되었습니다.");
            } else {
                resultMap.put("resultCode", "0");
                resultMap.put("resultMsg", "퀴즈를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            resultMap.put("resultCode", "0");
            resultMap.put("resultMsg", "퀴즈 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
        return resultMap;
    }

    public void hintUpdate(Hint hint) {
        Optional<Hint> optionalHint = hintRepository.findById(hint.getHintId());
        if (optionalHint.isPresent()) {
            Hint existingHint = optionalHint.get();
            // 업데이트된 값을 설정
            existingHint.setHint(hint.getHint());

            // 업데이트된 Games 객체 저장
            hintRepository.save(existingHint);
        }
    }

    public Map<String, String> quizDelete(Long gameId, Map<String, String> resultMap) {
        try {
            //games의 active를 false로 변경
            Optional<Games> optionalGames = gameRepository.findById(gameId);
            if (optionalGames.isPresent()) {
                Games existingGames = optionalGames.get();
                existingGames.setActive(false);

                gameRepository.save(existingGames);
            } else {
                resultMap.put("resultCode", "0");
                resultMap.put("resultMsg", "퀴즈를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            resultMap.put("resultCode", "0");
            resultMap.put("resultMsg", "퀴즈 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return resultMap;
    }

}
