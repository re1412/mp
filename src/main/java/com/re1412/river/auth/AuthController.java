package com.re1412.river.auth;

import com.re1412.river.user.Users;
import org.slf4j.Logger;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String login() throws Exception
    {
        return "views/index";
    }

    @ResponseBody
    @PostMapping("/join")
    public Map<String, Boolean> joinUser(Users user) {
        authService.joinUser(user);
        Map<String, Boolean> joinResult = new HashMap<>();
        joinResult.put("result", true);
        return joinResult;
    }

    @ResponseBody
    @GetMapping("/join/confirm")
    public Map<String, Boolean> joinUserConfirm(String loginId) {
        boolean idDuplicate = authService.findByLoginId(loginId);
        Map<String, Boolean> joinResult = new HashMap<>();
        joinResult.put("result", idDuplicate);
        return joinResult;
    }

    /**
     * 로그인 실패 폼
     * @return
     */
    @GetMapping("/denied")
    public String accessDenied(Model model) {
        model.addAttribute("login","아이디 또는 비밀번호를 잘못 입력했습니다.\n");
        return "views/index";
    }

    /**
     * 유저 페이지
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/main")
    public String userAccess(Model model, Authentication authentication, HttpServletRequest request) throws Exception{
        try {
            //Authentication 객체를 통해 유저 정보를 가져올 수 있다.
            Users user = (Users) authentication.getPrincipal();  //userDetail 객체를 가져옴
            String loginIP = authService.getClientIP(request);
            log.info("[INFO] login ID : {}, IP : {}", user.getLoginId(), loginIP);
            return "redirect:/quiz";
        }catch (Exception e){
            return "views/index";
        }
    }
}