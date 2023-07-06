package fithub.app.converter;

import fithub.app.domain.User;
import fithub.app.domain.enums.SocialType;
import fithub.app.exception.common.ErrorCode;
import fithub.app.exception.handler.UserException;
import fithub.app.repository.UserRepository;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final UserRepository userRepository;

    private static UserRepository staticUserRepository;

    @PostConstruct
    public void init() {
        this.staticUserRepository = this.userRepository;
    }

    public static User toCreateOAuthUser(String socialId, SocialType socialType){
        return User.builder()
                .socialId(socialId)
                .socialType(socialType)
                .isSocial(true)
                .build();
    }

    public static User toUser(Long userId){
        return staticUserRepository.findById(userId).orElseThrow(()->new UserException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public static UserResponseDto.OauthDto toOauthDto(OAuthResult.OAuthResultDto result){
        return UserResponseDto.OauthDto.builder()
                .accessToken(result.getJwt())
                .isLogin(result.getIsLogin())
                .build();
    }
}
