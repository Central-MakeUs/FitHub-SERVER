package fithub.app.service.impl;

import fithub.app.auth.provider.TokenProvider;
import fithub.app.converter.UserConverter;
import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.User;
import fithub.app.domain.enums.SocialType;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.repository.UserRepository;
import fithub.app.service.UserService;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.requestDto.UserRequestDto;
import fithub.app.web.dto.responseDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private final TokenProvider tokenProvider;

    private User createUser(String socialId, SocialType socialType) {
        User newUser = UserConverter.toCreateOAuthUser(socialId, socialType);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    @Transactional
    public OAuthResult.OAuthResultDto kakaoOAuth(String socialId) {
        SocialType socialType = SocialType.KAKAO;

        Boolean isLogin = true;
        String jwt = null;
        Optional<User> userOptional = userRepository.findBySocialIdAndSocialType(socialId, socialType);

        if (!userOptional.isPresent()){
            isLogin = false;
            User newUser = userRepository.save(
                    User.builder()
                            .isSocial(true)
                            .socialId(socialId)
                            .socialType(socialType)
                            .build()
            );
            
            jwt = tokenProvider.createAccessToken(newUser.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        else{
            User user = userOptional.get();
            jwt = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }

        return OAuthResult.OAuthResultDto.builder()
                .isLogin(isLogin)
                .jwt(jwt)
                .build();

    }

    @Override
    public Optional<User> checkExistNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Override
    public List<ExerciseCategory> getExerciseList() {
        return exerciseCategoryRepository.findAll();
    }

    @Override
    public User signUpPhoneNum(UserRequestDto.UserInfo request) {
        return null;
    }

    public OAuthResult.OAuthResultDto appleOAuth(String socialId){
        SocialType socialType = SocialType.APPLE;

        Boolean isLogin = true;
        String jwt = null;
        Optional<User> userOptional = userRepository.findBySocialIdAndSocialType(socialId, socialType);

        if (!userOptional.isPresent()){
            isLogin = false;
            User newUser = userRepository.save(
                    User.builder()
                            .isSocial(true)
                            .socialId(socialId)
                            .socialType(socialType)
                            .build()
            );

            jwt = tokenProvider.createAccessToken(newUser.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        else{
            User user = userOptional.get();
            jwt = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }

        return OAuthResult.OAuthResultDto.builder()
                .isLogin(isLogin)
                .jwt(jwt)
                .build();
    }


//    @Override
//    @Transactional
//    public String oAuthLogin(String socialId, String provider) {
//
//        SocialType socialType = SocialType.valueOf(provider);
//        User user = userRepository.findByEmailAndSocialType(socialId, socialType).orElseGet(() -> createUser(socialId, socialType));
//        String jwt = tokenProvider.createAccessToken(user.getId(),String.valueOf(socialType),socialId,Arrays.asList(new SimpleGrantedAuthority("USER")));
//
//        return jwt;
//    }
}
