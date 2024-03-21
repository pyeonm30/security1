package kr.basic.security.config.auth;

import kr.basic.security.entity.User;
import kr.basic.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Security 설정에서 loginProcessUrl("/login");
// login 요청이 들어오면 자동으로 UserDetailsService 타입으로
// IoC되어 있는 loadUserByUsername 함수가 실행됨
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    // Security Session = Authentication = UserDetails
    // Security Session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // String username은 loginForm.html에서 넘어온 input name="username"
        User userEntity = userRepository.findByUsername(username);
        if(userEntity!=null){       // username으로 찾은 userEntity가 존재한다면
            System.out.println("유저디테일 객체 생성 userEntity = " + userEntity);
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
