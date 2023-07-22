package fithub.app.service.impl;

import fithub.app.auth.provider.TokenProvider;
import fithub.app.base.Code;
import fithub.app.converter.ExercisePreferenceConverter;
import fithub.app.converter.UserConverter;
import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.User;
import fithub.app.domain.enums.SocialType;
import fithub.app.domain.mapping.ExercisePreference;
import fithub.app.base.exception.handler.UserException;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.repository.ExercisePreferenceRepository;
import fithub.app.repository.UserRepository;
import fithub.app.service.UserService;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.requestDto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private final ExercisePreferenceRepository exercisePreferenceRepository;

    private final PasswordEncoder passwordEncoder;

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

        User user;

        if (!userOptional.isPresent()){
            isLogin = false;
            user = userRepository.save(
                    User.builder()
                            .isSocial(true)
                            .socialId(socialId)
                            .socialType(socialType)
                            .build()
            );
            
            jwt = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        else{
            user = userOptional.get();
            jwt = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }

        return OAuthResult.OAuthResultDto.builder()
                .isLogin(isLogin)
                .jwt(jwt)
                .userId(user.getId())
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
    @Transactional(readOnly = false)
    public User signUpPhoneNum(UserRequestDto.UserInfo request) {
        User newUser = UserConverter.toUserPhoneNum(request);
        User savedUser = userRepository.save(newUser);

        for (int i = 0; i < request.getPreferExercises().size(); i++) {
            ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(request.getPreferExercises().get(i))
                    .orElseThrow(()->new UserException(Code.NO_EXERCISE_CATEGORY_EXIST));
            ExercisePreference exercisePreference = ExercisePreferenceConverter.toExercisePreference(savedUser, exerciseCategory);
            exercisePreferenceRepository.save(exercisePreference);
        }

        return savedUser;
    }

    @Override
    public User findByPhoneNum(String phoneNum) {
        User user = userRepository.findByPhoneNum(phoneNum).orElseThrow(()-> new UserException(Code.NO_PHONE_USER));
        return user;
    }

    public void findByPhoneNumJoin(String phoNum){
        Optional<User> byPhoneNum = userRepository.findByPhoneNum(phoNum);
        if(byPhoneNum.isPresent())
            throw new UserException(Code.EXIST_PHONE_USER);
    }

    @Override
    public String login(User user, String password) {
        System.out.println(password);
        String jwt = null;
        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new UserException(Code.PASSWORD_ERROR);
        else
            jwt = tokenProvider.createAccessToken(user.getId(), user.getPhoneNum(), Arrays.asList(new SimpleGrantedAuthority("USER")));
        return jwt;
    }

    @Override
    @Transactional(readOnly = false)
    public User socialInfoComplete(UserRequestDto.UserOAuthInfo request, User user) {
        User updatedUser =  UserConverter.toSocialUser(request, user);
        for (int i = 0; i < request.getPreferExercises().size(); i++) {
            ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(request.getPreferExercises().get(i))
                    .orElseThrow(()->new UserException(Code.NO_EXERCISE_CATEGORY_EXIST));
            ExercisePreference exercisePreference = ExercisePreferenceConverter.toExercisePreference(updatedUser, exerciseCategory);
            exercisePreferenceRepository.save(exercisePreference);
        }

        return updatedUser;
    }

    @Override
    @Transactional(readOnly = false)
    public User updatePassword(String phoneNum,String password) {
        User user = userRepository.findByPhoneNum(phoneNum).orElseThrow(() ->new UserException(Code.NO_PHONE_USER));
        String encodedPassword = passwordEncoder.encode(password);
        User updatedUser = user.setPassword(encodedPassword);
        return updatedUser;
    }

    @Transactional(readOnly = false)
    public OAuthResult.OAuthResultDto appleOAuth(String socialId){
        SocialType socialType = SocialType.APPLE;

        Boolean isLogin = true;
        String jwt = null;
        User user;

        Optional<User> userOptional = userRepository.findBySocialIdAndSocialType(socialId, socialType);

        if (!userOptional.isPresent()){
            isLogin = false;
            user = userRepository.save(
                    User.builder()
                            .isSocial(true)
                            .socialId(socialId)
                            .socialType(socialType)
                            .build()
            );

            jwt = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        else{
            user = userOptional.get();
            jwt = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }

        return OAuthResult.OAuthResultDto.builder()
                .isLogin(isLogin)
                .jwt(jwt)
                .userId(user.getId())
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
