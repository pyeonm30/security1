package kr.basic.security.controller;

import kr.basic.security.entity.RoleUser;
import kr.basic.security.entity.User;
import kr.basic.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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
}
