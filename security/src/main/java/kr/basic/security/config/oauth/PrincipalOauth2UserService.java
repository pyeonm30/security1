package kr.basic.security.config.oauth;

import kr.basic.security.config.auth.PrincipalDetails;
import kr.basic.security.entity.RoleUser;
import kr.basic.security.entity.User;
import kr.basic.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {


	private final UserRepository userRepository;


	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	// userRequest 는 code를 받아서 accessToken을 응답 받은 객체
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


		System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
		System.out.println("userRequest getAccessToken : " + userRequest.getAccessToken().getTokenValue());


		OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
		System.out.println("getAttributes : " +  oAuth2User.getAttributes());

		// 회원가입 강제로 진행하기
		String provider = userRequest.getClientRegistration().getClientId();
		String providerId = oAuth2User.getAttribute("sub");
		String username = provider+"_"+providerId; // google_123451234~
		String password = bCryptPasswordEncoder.encode("test");
		String email = oAuth2User.getAttribute("email");


		// 무조건 회원가입 하면 안됨 회원이미 있는지 확인후 진행
		User user = userRepository.findByUsername(username);
		if(user == null){
			user = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(RoleUser.USER)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(user);
		}
// 이렇게 하면 loadUser 할때 이 객체가 Authentication 객체 안에 들어감
	  return new PrincipalDetails(user,oAuth2User.getAttributes());  // 이 함수 종료할때 @AuthenticationProncpal 어노테이션의 객체가 만들어진다
	}


}
