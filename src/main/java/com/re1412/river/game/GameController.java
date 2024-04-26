package com.re1412.river.game;

import com.re1412.river.common.SHAEncryption;
import com.re1412.river.hint.Hint;
import com.re1412.river.score.Score;
import com.re1412.river.score.UserScore;
import com.re1412.river.user.UserInfo;
import com.re1412.river.user.UserService;
import com.re1412.river.user.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private static final String NULL_ID = "0";
    private static final String NULL_VALUE = "";

    @GetMapping("/quiz")
    public String quizList(@RequestParam(defaultValue = "0") int page, Model model, Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        Page<Games> games = gameService.gameListAll(page);
        UserInfo userInfo = userService.userInfo(user.getUserId());
        List<UserScore> scoreInfo = userService.userScoreList();
        model.addAttribute("games", games);
        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("scoreInfo", scoreInfo);

        return "views/quiz/quizList";
    }

    @GetMapping("/quiz/new")
    public String quizNew(Model model, Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        List<Games> games = gameService.gameList(user.getUserId());
        model.addAttribute("games", games);
        model.addAttribute("user", user);

        return "views/quiz/quizManagement :: .content";
    }

    @ResponseBody
    @Transactional
    @PostMapping("/quiz/new")
    public Map<String, String> quizInsert(@RequestBody Map<String, String[]> quizList, Authentication authentication) throws NoSuchAlgorithmException {
        Map<String, String> resultMap = new HashMap<>();
        try {
            for (int i = 0; i < quizList.get("question").length; i++) {
                // 등록할 로우 데이터 확인(gameId가 "0"이 아닐 시 등록된 데이터)
                if (!NULL_ID.equals(quizList.get("gameId")[i])) {
                    continue;
                }
                // question & answer 값이 있을 경우 등록
                if (NULL_VALUE.equals(quizList.get("question")[i]) || NULL_VALUE.equals(quizList.get("answer")[i])) {
                    continue;
                }
                Games games = new Games();
                games.setDescription("quiz");
                games.setQuestion(quizList.get("question")[i]);
                games.setAnswer(quizList.get("answer")[i]);
                games.setEncryptedAnswer(SHAEncryption.encrypt(quizList.get("answer")[i]));
                games.setUsers((Users) authentication.getPrincipal());
                games.setActive(true);
                // hint 값이 있을 경우 등록
                if (!NULL_VALUE.equals(quizList.get("hint")[i])) {
                    Hint hint = new Hint();
                    hint.setHint(quizList.get("hint")[i]);
                    gameService.hintInsert(hint);
                    games.setHint(hint);
                }
                Long gameId = gameService.quizInsert(games);
                log.info("insert game data, game Id : {}", gameId);
            }
            resultMap.put("resultCode", "1");
            resultMap.put("resultMsg", "등록이 완료되었습니다.");
        } catch (Exception e){
            resultMap.put("resultCode", "0");
            resultMap.put("resultMsg", e.toString());
            log.error("quizInsert Error = {}", e.toString());
            throw e;
        }
        return resultMap;
    }

    @ResponseBody
    @Transactional
    @PatchMapping("/quiz/new")
    public Map<String, String> quizUpdate(@RequestBody Quiz quiz) throws NoSuchAlgorithmException {
        Map<String, String> resultMap = new HashMap<>();
        try {
            // 클라이언트에서 전달된 Quiz 객체를 Games 객체로 변환
            Games games = new Games();
            games.setGameId(quiz.getGameId()); // 게임 ID 설정
            games.setQuestion(quiz.getQuestion());
            games.setAnswer(quiz.getAnswer());
            games.setEncryptedAnswer(SHAEncryption.encrypt(quiz.getAnswer()));

            Hint hint = new Hint();
            if (quiz.getHintId() == null) {
                hint.setHint(quiz.hint);
                gameService.hintInsert(hint);
            } else {
                hint.setHintId(quiz.hintId);
                hint.setHint(quiz.hint);
                gameService.hintUpdate(hint);
            }
            games.setHint(hint);

            // Spring Data JPA를 사용하여 수정할 Games 객체 검색
            resultMap = gameService.quizUpdate(games, resultMap);
        } catch (Exception e){
            resultMap.put("resultCode", "0");
            resultMap.put("resultMsg", e.toString());
            log.error("quizUpdate Error = {}", e.toString());
            throw e;
        }
        return resultMap;
    }

    @ResponseBody
    @DeleteMapping("/quiz/new")
    public Map<String, String> quizDelete(@RequestBody Long gameId){
        Map<String, String> resultMap = new HashMap<>();
        resultMap = gameService.quizDelete(gameId, resultMap);
        return resultMap;
    }

    @ResponseBody
    @Transactional
    @PostMapping("/quiz/rank")
    public Map<String, String> quizAnswerRank(@RequestBody Quiz quiz){
        Map<String, String> resultMap = new HashMap<>();
        try {
            Score score = new Score();
            Users user = userService.findUser(quiz.userId);
            score.setUsers(user);

            Games game = gameService.findGame(quiz);
            if (game.getSolver() == null) {
                resultMap.put("solver", user.getName());
                resultMap.put("answer", game.getAnswer());
                resultMap.put("point", "5");
                game.setSolver(quiz.getUserId());
                gameService.gameSave(game);
                score.setScorePoint(5);
            } else {
                Users solvedUser = userService.findUser(game.getSolver());
                resultMap.put("solver", solvedUser.getName());
                resultMap.put("answer", game.getAnswer());
                resultMap.put("point", "1");
                score.setScorePoint(1);
            }
            score.setGames(game);

            gameService.scoreInsert(score);
        } catch (EntityNotFoundException e) {
            resultMap.put("resultMsg", e.toString());
            throw new EntityNotFoundException(e.toString());
        }
        return resultMap;
    }
}
