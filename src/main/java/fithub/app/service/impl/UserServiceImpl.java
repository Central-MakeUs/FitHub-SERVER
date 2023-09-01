package fithub.app.service.impl;

import fithub.app.auth.provider.TokenProvider;
import fithub.app.aws.s3.AmazonS3Manager;
import fithub.app.base.Code;
import fithub.app.converter.ExercisePreferenceConverter;
import fithub.app.converter.UserConverter;
import fithub.app.domain.*;
import fithub.app.domain.enums.SocialType;
import fithub.app.domain.mapping.ExercisePreference;
import fithub.app.base.exception.handler.UserException;
import fithub.app.domain.mapping.UserReport;
import fithub.app.repository.*;
import fithub.app.repository.ArticleRepositories.ArticleRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.service.UserService;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.requestDto.UserRequestDto;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    private final ArticleRepository articleRepository;

    private final RecordRepository recordRepository;

    private final UserExerciseRepository userExerciseRepository;

    private final UserReportRepository userReportRepository;

    private final NotificationRepository notificationRepository;
    private final AmazonS3Manager s3Manager;

    private final FcmTokenRepository fcmTokenRepository;

    private final BestRecorderRepository bestRecorderRepository;

    @Value("${paging.size}")
    private Integer size;

    private User createUser(String socialId, SocialType socialType) {
        User newUser = UserConverter.toCreateOAuthUser(socialId, socialType);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    @Transactional
    public OAuthResult.OAuthResultDto kakaoOAuth(String socialId, String fcmToken) {
        SocialType socialType = SocialType.KAKAO;

        Boolean isLogin = true;
        String accessToken = null;
        Optional<User> userOptional = userRepository.findBySocialIdAndSocialType(socialId, socialType);

        User user;

        if (!userOptional.isPresent()){
            isLogin = false;
            user = userRepository.save(
                    User.builder()
                            .isSocial(true)
                            .socialId(socialId)
                            .socialType(socialType)
                            .profileUrl("https://cmc-fithub.s3.ap-northeast-2.amazonaws.com/profile/%EA%B8%B0%EB%B3%B8+%EC%9D%B4%EB%AF%B8%EC%A7%80.png")
                            .communityPermit(Boolean.TRUE)
                            .fcmTokenList(new ArrayList<>())
                            .isDefaultProfile(true)
                            .build()
            );

            accessToken = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        else{
            user = userOptional.get();
            if (user.getNickname() == null || user.getMainExercise() == null)
                isLogin = false;
            else
            {
                Optional<FcmToken> findToken = fcmTokenRepository.findByToken(fcmToken);

                if(!findToken.isPresent()) {
                    FcmToken savedToken = fcmTokenRepository.save(FcmToken.builder()
                            .user(user)
                            .token(fcmToken)
                            .build());
                    savedToken.setUser(user);
                }
            }
            accessToken = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }

        return OAuthResult.OAuthResultDto.builder()
                .isLogin(isLogin)
                .accessToken(accessToken)
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
    public User signUpPhoneNum(UserRequestDto.UserInfo request) throws IOException
    {
        User newUser = UserConverter.toUserPhoneNum(request);
        User savedUser = userRepository.save(newUser);

        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(request.getPreferExercises())
                .orElseThrow(()->new UserException(Code.NO_EXERCISE_CATEGORY_EXIST));
        ExercisePreference exercisePreference = ExercisePreferenceConverter.toExercisePreference(savedUser, exerciseCategory);
        exercisePreferenceRepository.save(exercisePreference);

        Optional<FcmToken> token = fcmTokenRepository.findByToken(request.getFcmToken());
        if(token.isEmpty()) {
            FcmToken fcmToken = fcmTokenRepository.save(FcmToken.builder()
                .token(request.getFcmToken())
                .user(savedUser)
                .build());
            fcmToken.setUser(savedUser);
        }
        else
            token.get().setUser(savedUser);

        return UserConverter.toCompleteUser(savedUser, exerciseCategory);
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
    public void findByPhoneNumPassChange(String phoneNum) {
        Optional<User> byPhoneNum = userRepository.findByPhoneNum(phoneNum);
        if(byPhoneNum.isEmpty())
            throw new UserException(Code.NO_PHONE_USER);
    }

    @Override
    @Transactional
    public String login(User user, String password, String fcmToken) {
        System.out.println(password);
        String jwt = null;
        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new UserException(Code.PASSWORD_ERROR);
        else {
            jwt = tokenProvider.createAccessToken(user.getId(), user.getPhoneNum(), Arrays.asList(new SimpleGrantedAuthority("USER")));
            Optional<FcmToken> byToken = fcmTokenRepository.findByToken(fcmToken);
            if(byToken.isEmpty()) {
                FcmToken token = fcmTokenRepository.save(FcmToken.builder()
                        .user(user)
                        .token(fcmToken)
                        .build());
                logger.info("토큰 추가 완료!");
                token.setUser(user);
            }
            else
                byToken.get().setUser(user);
        }
        return jwt;
    }

    @Override
    @Transactional(readOnly = false)
    public User socialInfoComplete(UserRequestDto.UserOAuthInfo request, User user) throws IOException
    {
        User updatedUser =  UserConverter.toSocialUser(request, user);

        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(request.getPreferExercises())
                .orElseThrow(()->new UserException(Code.NO_EXERCISE_CATEGORY_EXIST));
        ExercisePreference exercisePreference = ExercisePreferenceConverter.toExercisePreference(updatedUser, exerciseCategory);
        exercisePreferenceRepository.save(exercisePreference);

        Optional<FcmToken> token = fcmTokenRepository.findByToken(request.getFcmToken());

        if(token.isEmpty()) {
            FcmToken fcmToken = fcmTokenRepository.save(FcmToken.builder()
                    .token(request.getFcmToken())
                    .user(user)
                    .build());

            fcmToken.setUser(user);
        }
        else
            token.get().setUser(user);

        return UserConverter.toCompleteUser(updatedUser, exerciseCategory);
    }

    @Override
    public Page<Article> getMyArticlesNoCategory(Integer pageIndex, User user) {
        Page<Article> articles = null;

        pageIndex = pageIndex == null ? 0 : pageIndex;

        articles = articleRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(pageIndex,size));

        return articles;
    }

    @Override
    public Page<Article> getMyArticles(Integer pageIndex, User user, Integer categoryId) {
        Page<Article> articles = null;

        pageIndex = pageIndex == null ? 0 : pageIndex;

        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new UserException(Code.CATEGORY_ERROR));
        articles = articleRepository.findAllByUserAndExerciseCategoryOrderByCreatedAtDesc(user, exerciseCategory,PageRequest.of(pageIndex,size));

        return articles;
    }

    @Override
    public Page<Record> getMyRecordsNoCategory(Integer pageIndex, User user) {
        Page<Record> records = null;

        pageIndex = pageIndex == null ? 0 : pageIndex;

        records = recordRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(pageIndex,size));
        return records;
    }

    @Override
    public Page<Record> getMyRecords(Integer pageIndex, User user, Integer categoryId) {
        Page<Record> records = null;

        pageIndex = pageIndex == null ? 0 : pageIndex;

        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new UserException(Code.CATEGORY_ERROR));
        records = recordRepository.findAllByUserAndExerciseCategoryOrderByCreatedAtDesc(user,exerciseCategory ,PageRequest.of(pageIndex,size));

        return records;
    }

    @Override
    public List<UserExercise> getMyExercises(User user) {
        List<UserExercise> myExercises = new ArrayList<>();

        myExercises.add(user.getMainExercise());

        List<UserExercise> userExerciseList = userExerciseRepository.findUserExercise(user);

        for (int i = 0; i < userExerciseList.size(); i++){
            if (userExerciseList.get(i).getExerciseCategory().getName().equals(myExercises.get(0).getExerciseCategory().getName()))
                continue;
            else
                myExercises.add(userExerciseList.get(i));
        }

        return myExercises;
    }

    @Override
    @Transactional(readOnly = false)
    public UserExercise patchMainExercise(User user, Integer categoryId) {
        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new UserException(Code.CATEGORY_ERROR));
        UserExercise target = userExerciseRepository.findByUserAndExerciseCategory(user, exerciseCategory).get();
        user.setMainExercise(target);
        return target;
    }

    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(Code.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public UserReport reportUser(Long userId, User user) {
        User target = userRepository.findById(userId).orElseThrow(() -> new UserException(Code.MEMBER_NOT_FOUND));
        if (target.getId().equals(user.getId()))
            throw new UserException(Code.SELF_REPORT);
        Optional<UserReport> findReport = userReportRepository.findByUserAndReporter(target, user);
        if(findReport.isPresent())
            throw new UserException(Code.ALREADY_REPORT);
        target.countReport();
        return userReportRepository.save(
            UserReport.builder()
                    .reporter(user)
                    .user(target)
                    .build()
        );
    }

    @Override
    public Page<Article> findUserArticle(Long userId, Integer categoryId, Integer pageIndex) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(Code.MEMBER_NOT_FOUND));
        ExerciseCategory exerciseCategory = null;
        if(categoryId != 0)
            exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new UserException(Code.CATEGORY_ERROR));

        return categoryId == 0 ? articleRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(pageIndex, size)) : articleRepository.findAllByUserAndExerciseCategoryOrderByCreatedAtDesc(user,exerciseCategory,PageRequest.of(pageIndex, size));
    }

    @Override
    public Page<Record> findUserRecord(Long userId, Integer categoryId, Integer pageIndex) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(Code.MEMBER_NOT_FOUND));
        ExerciseCategory exerciseCategory = null;
        if(categoryId != 0)
            exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new UserException(Code.CATEGORY_ERROR));

        return categoryId == 0 ? recordRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(pageIndex, size)) : recordRepository.findAllByUserAndExerciseCategoryOrderByCreatedAtDesc(user,exerciseCategory,PageRequest.of(pageIndex, size));
    }

    @Override
    public Page<Article> findSavedArticle(Integer categoryId, Integer pageIndex, User user) {
        ExerciseCategory exerciseCategory = null;
        if(categoryId != 0)
            exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new UserException(Code.CATEGORY_ERROR));

        return categoryId == 0 ? articleRepository.findAllSavedArticle(user, user,user,PageRequest.of(pageIndex, size)) : articleRepository.findAllSavedArticleCategory(user,user,user,exerciseCategory,PageRequest.of(pageIndex, size));
    }

    @Override
    @Transactional
    public String changeMyProfile(User user, UserRequestDto.ChangeMyProfileDto request) throws IOException
    {
        User findUser = userRepository.findById(user.getId()).get();
        Uuid uuid = s3Manager.createUUID();
        String KeyName = s3Manager.generateRecordKeyName(uuid, request.getNewProfile().getOriginalFilename());
        String fileUrl = s3Manager.uploadFile(KeyName, request.getNewProfile());
        logger.info("S3에 업로드 한 파일의 url : {}", fileUrl);
        findUser.setProfile(fileUrl);
        return fileUrl;
    }

    @Override
    @Transactional
    public void changeMyProfileDefault(User user) {
        User findUser = userRepository.findById(user.getId()).get();
        findUser.setProfileDefault();
    }

    @Override
    public Long checkRemainAlarm(User user) {
        Long remainAlarm = notificationRepository.findRemainAlarm(user);
        return remainAlarm;
    }

    @Override
    public User findUserNotBlocked(Long userId, User user) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(Code.MEMBER_NOT_FOUND));
        Long count = userReportRepository.checkReport(findUser, user);
        if (count > 0)
            throw new UserException(Code.BLOCKED_USER);
        return findUser;
    }

    @Override
    @Transactional
    public void addFcmToken(User user, String token) {
        FcmToken fcmToken = fcmTokenRepository.save(FcmToken.builder()
                .user(user)
                .token(token)
                .build());
        fcmToken.setUser(user);
    }

    @Override
    @Transactional
    public void quitMember(User user) {
        User quitUser = userRepository.findById(user.getId()).orElseThrow(()->new UserException(Code.MEMBER_NOT_FOUND));
//        Optional<BestRecorder> byUserId = bestRecorderRepository.findByUserId(user.getId());
//        if(byUserId.isPresent())
//
        List<UserReport> userReport = userReportRepository.findAllByReporter(user);
        for(UserReport report : userReport){
            userReportRepository.delete(report);
        }
        userReportRepository.flush();
        userRepository.delete(quitUser);
    }

    public Boolean checkPass(User user, UserRequestDto.CheckPassDto request) {
        String password = user.getPassword();
        if (!passwordEncoder.matches(request.getPassword(), password))
            return false;
        return true;
    }

    @Override
    public void checkBlock(User user, Long userId) {
        Optional<UserReport> findResult = userReportRepository.findByReporterAndUser(user, userRepository.findById(userId).get());
        if(findResult.isPresent())
            throw new UserException(Code.BLOCKED_USER);
    }

    @Override
    @Transactional
    public String changePassToken(User user, UserRequestDto.ChangePassTokenDto request) {
        User target = userRepository.findById(user.getId()).get();
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        logger.info("바뀐 pass : {}", encodedPassword);
        User updatedUser = user.setPassword(encodedPassword);
        return request.getNewPassword();
    }

    @Override
    @Transactional
    public void logoutService(User user) {
        List<FcmToken> userToken = fcmTokenRepository.findAllByUser(user);
        userToken.stream()
                .map(fcmToken -> {fcmTokenRepository.delete(fcmToken); return null;}).collect(Collectors.toList());
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
    public OAuthResult.OAuthResultDto appleOAuth(String socialId, String fcmToken, String userName){
        SocialType socialType = SocialType.APPLE;

        Boolean isLogin = true;
        String accessToken = null;
        User user;

        Optional<User> userOptional = userRepository.findBySocialIdAndSocialType(socialId, socialType);

        if (!userOptional.isPresent()){
            isLogin = false;
            user = userRepository.save(
                    User.builder()
                            .isSocial(true)
                            .socialId(socialId)
                            .socialType(socialType)
                            .profileUrl("https://cmc-fithub.s3.ap-northeast-2.amazonaws.com/profile/%EA%B8%B0%EB%B3%B8+%EC%9D%B4%EB%AF%B8%EC%A7%80.png")
                            .communityPermit(Boolean.TRUE)
                            .fcmTokenList(new ArrayList<>())
                            .isDefaultProfile(true)
                            .build()
            );

            accessToken = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType),socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        else {
            user = userOptional.get();
            if (user.getNickname() == null || user.getMainExercise() == null) {
                isLogin = false;
            } else {
                Optional<FcmToken> findToken = fcmTokenRepository.findByToken(fcmToken);

                if (!findToken.isPresent()) {
                    FcmToken savedToken = fcmTokenRepository.save(FcmToken.builder()
                            .user(user)
                            .token(fcmToken)
                            .build());
                    savedToken.setUser(user);
                }
                accessToken = tokenProvider.createAccessToken(user.getId(), String.valueOf(socialType), socialId, Arrays.asList(new SimpleGrantedAuthority("USER")));
            }
        }

        return OAuthResult.OAuthResultDto.builder()
                .isLogin(isLogin)
                .accessToken(accessToken)
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
