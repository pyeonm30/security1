package kr.basic.security.controller;

import kr.basic.security.config.auth.PrincipalDetails;
import kr.basic.security.entity.RoleUser;
import kr.basic.security.entity.User;
import kr.basic.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping({ "", "/" })
    public @ResponseBody String index() {
        return "인덱스 페이지입니다.";
    }
    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }
    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }
    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 스프링시큐리티의 기본 login uri로 맵핑됨!
    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String join() {
        return "joinForm";
    }
    @PostMapping("/join")
    public String join(User user) {

        user.setRole(RoleUser.USER);
        System.out.println("user = " + user);
        String initPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(initPassword);
        user.setPassword(encPassword);

        User u = userRepository.save(user);  // 이렇게 되면 시큐리티로 로그인 할수없음 => 패스워드 암호화 필수
        System.out.println("u = " + u);
        return "redirect:/loginForm";
    }
    /* 로그인 */
    @GetMapping("/auth/login")
    public @ResponseBody String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {

        /* 에러와 예외를 모델에 담아 view resolve */
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return error.toString() + exception.toString();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody  String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')  ")
    @GetMapping("/userData")
    public @ResponseBody  String userData() {
        return "유저 데이터 정보 ";
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myData")
    public @ResponseBody  String myData() {
        return "내 정보 ";
    }

    @GetMapping("/test/login")
    public @ResponseBody Authentication testLogin(Authentication authentication,
                                          @AuthenticationPrincipal UserDetails userDetails){
        if(authentication == null){
            return null;
        }
        System.out.println("=================");
        System.out.println("authentication=" + authentication);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("userDetails=" + userDetails );

        return authentication;

    }
}
