package fithub.app.converter;

import fithub.app.auth.provider.TokenProvider;
import fithub.app.aws.s3.AmazonS3Manager;
import fithub.app.base.Code;
import fithub.app.domain.*;
import fithub.app.domain.enums.Gender;
import fithub.app.domain.enums.SocialType;
import fithub.app.base.exception.handler.UserException;
import fithub.app.domain.mapping.UserReport;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.repository.GradeRepository;
import fithub.app.repository.UserExerciseRepository;
import fithub.app.repository.UserRepository;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.requestDto.UserRequestDto;
import fithub.app.web.dto.responseDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final UserRepository userRepository;

    private static UserRepository staticUserRepository;

    private final UserExerciseRepository userExerciseRepository;

    private static UserExerciseRepository staticUserExerciseRepository;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private static ExerciseCategoryRepository staticExerciseCategoryRepository;

    private final GradeRepository gradeRepository;

    private static GradeRepository staticGradeRepository;

    private final TokenProvider tokenProvider;

    private static TokenProvider staticTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private static PasswordEncoder staticPasswordEncoder;

    private final AmazonS3Manager amazonS3Manager;

    private static AmazonS3Manager staticAmazonS3Manager;

    @PostConstruct
    public void init() {
        this.staticUserRepository = this.userRepository;
        this.staticPasswordEncoder = this.passwordEncoder;
        this.staticTokenProvider = this.tokenProvider;
        this.staticAmazonS3Manager = this.amazonS3Manager;
        this.staticUserExerciseRepository = this.userExerciseRepository;
        this.staticExerciseCategoryRepository = this.exerciseCategoryRepository;
        this.staticGradeRepository = this.gradeRepository;
    }

    public static User toCreateOAuthUser(String socialId, SocialType socialType){
        User newUser = User.builder()
                .socialId(socialId)
                .socialType(socialType)
                .isSocial(true)
                .monthlyRecordNum(0L)
                .totalRecordNum(0L)
                .contiguousRecordNum(0L)
                .build();
        newUser.setUserExerciseList(toUserExerciseList(newUser));
        return newUser;
    }

    public static List<UserExercise> toUserExerciseList(User user){
        List<ExerciseCategory> exerciseCategoryList = staticExerciseCategoryRepository.findAll();
        Grade grade = staticGradeRepository.findByName("우주먼지").get();
        return exerciseCategoryList.stream()
                .map(
                        exerciseCategory ->
                                UserExercise
                                        .builder()
                                        .user(user)
                                        .grade(grade)
                                        .exerciseCategory(exerciseCategory)
                                        .build()

                ).collect(Collectors.toList());
    }

    public static User toUserPhoneNum(UserRequestDto.UserInfo request) throws IOException
    {

        String birthdayString = request.getBirth(); // 생년월일 문자열 (YYMMDD 형식)

        // 생년월일 문자열을 LocalDate로 변환
        String birth = birthdayString.substring(0,2);

        String birthday = birthdayString.substring(2);
        // 현재 날짜를 가져옴
        LocalDate currentDate = LocalDate.now();

        // 생년월일과 현재 날짜를 기준으로 만 나이 계산
        int age = currentDate.getYear() % 100 - Integer.valueOf(birth)- 1;

        if (age < 0)
            age += 100;

        // 현재 연도를 가져옴
        int currentYear = LocalDate.now().getYear();

        // 날짜 문자열을 LocalDate로 변환
        LocalDate date = LocalDate.parse(birthdayString, DateTimeFormatter.ofPattern("yyMMdd"));

        // 생년월일에 현재 연도를 설정하여 완전한 날짜로 만듦
        LocalDate completeDate = date.withYear(currentYear);

        age = ChronoUnit.DAYS.between(completeDate, currentDate) >= 0 ? age + 1 : age;

        System.out.println(age);


        String genderFlag = request.getGender();

        Gender gender = Integer.valueOf(genderFlag) % 2 == 0 ? Gender.FEMALE : Gender.MALE;

        User newUser = User.builder()
                .marketingAgree(request.getMarketingAgree())
                .phoneNum(request.getPhoneNumber())
                .name(request.getName())
                .isSocial(false)
                .nickname(request.getNickname())
                .password(staticPasswordEncoder.encode(request.getPassword()))
                .age(age)
                .gender(gender)
                .monthlyRecordNum(0L)
                .totalRecordNum(0L)
                .contiguousRecordNum(0L)
                .profileUrl(request.getProfileImage() == null ? "https://cmc-fithub.s3.ap-northeast-2.amazonaws.com/profile/%EA%B8%B0%EB%B3%B8+%EC%9D%B4%EB%AF%B8%EC%A7%80.png" : uploadProfileImage(request.getProfileImage()))
                .build();

        newUser.setUserExerciseList(toUserExerciseList(newUser));
        return newUser;
    }

    public static String uploadProfileImage(MultipartFile recordImage) throws IOException
    {
        Uuid uuid = staticAmazonS3Manager.createUUID();
        String KeyName = staticAmazonS3Manager.generateProfileName(uuid, recordImage.getOriginalFilename());
        String fileUrl = staticAmazonS3Manager.uploadFile(KeyName, recordImage);
        return fileUrl;
    }

    public static User toSocialUser(UserRequestDto.UserOAuthInfo request, User user) throws IOException
    {
        String birthdayString = request.getBirth(); // 생년월일 문자열 (YYMMDD 형식)

        // 생년월일 문자열을 LocalDate로 변환
        String birth = birthdayString.substring(0,2);

        String birthday = birthdayString.substring(2);
        // 현재 날짜를 가져옴
        LocalDate currentDate = LocalDate.now();

        // 생년월일과 현재 날짜를 기준으로 만 나이 계산
        int age = currentDate.getYear() % 100 - Integer.valueOf(birth)- 1;

        if (age < 0)
            age += 100;

        // 현재 연도를 가져옴
        int currentYear = LocalDate.now().getYear();

        // 날짜 문자열을 LocalDate로 변환
        LocalDate date = LocalDate.parse(birthdayString, DateTimeFormatter.ofPattern("yyMMdd"));

        // 생년월일에 현재 연도를 설정하여 완전한 날짜로 만듦
        LocalDate completeDate = date.withYear(currentYear);

        age = ChronoUnit.DAYS.between(completeDate, currentDate) >= 0 ? age + 1 : age;

        System.out.println(age);


        String genderFlag = request.getGender();

        Gender gender = Integer.valueOf(genderFlag) % 2 == 0 ? Gender.FEMALE : Gender.MALE;

        String profileUrl = request.getProfileImage() == null ? "https://cmc-fithub.s3.ap-northeast-2.amazonaws.com/profile/%EA%B8%B0%EB%B3%B8+%EC%9D%B4%EB%AF%B8%EC%A7%80.png" : uploadProfileImage(request.getProfileImage());

        User updatedUser = user.updateInfo(request, age, gender, profileUrl);
        updatedUser.setUserExerciseList(toUserExerciseList(updatedUser));
        return updatedUser;
    }

    public static User toCompleteUser(User user, ExerciseCategory exerciseCategory){
        UserExercise mainExercise = staticUserExerciseRepository.findByUserAndExerciseCategory(user, exerciseCategory).get();
        return user.setMainExercise(mainExercise);
    }

    public static User toUser(Long userId){
        return staticUserRepository.findById(userId).orElseThrow(()->new UserException(Code.MEMBER_NOT_FOUND));
    }

    public static UserResponseDto.OauthDto toOauthDto(OAuthResult.OAuthResultDto result){
        return UserResponseDto.OauthDto.builder()
                .accessToken(result.getAccessToken())
                .build();
    }

    public static UserResponseDto.CommunityUserInfo toCommunityUserInfo(User user){
        return UserResponseDto.CommunityUserInfo.builder()
                .ownerId(user.getId())
                .ProfileUrl(user.getProfileUrl())
                .nickname(user.getNickname())
                .mainExerciseInfo(UserExerciseConverter.toUserExerciseDto(user))
                .build();
    }

//    public static UserResponseDto.RecordUserDto toRecordUserDto(User user){
//        return UserResponseDto.RecordUserDto.builder()
//                .ownerId(user.getId())
//                .ProfileUrl(user.getProfileUrl())
//                .nickname(user.getNickname())
//                .mainExerciseInfo(UserExerciseConverter.toUserExerciseDto(user))
//                .build();
//    }

    public static UserResponseDto.JoinUserDto toJoinUserDto(User user){
        return UserResponseDto.JoinUserDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .accessToken(staticTokenProvider.createAccessToken(user.getId(), user.getPhoneNum(), Arrays.asList(new SimpleGrantedAuthority("USER"))))
                .build();
    }

    public static UserResponseDto.PassChangeDto toPassChangeDto(String newPass){
        return UserResponseDto.PassChangeDto.builder()
                .newPass(newPass)
                .build();
    }

    public static UserResponseDto.LoginResultDto toLoginDto(String jwt, User user){
        return UserResponseDto.LoginResultDto.builder()
                .accessToken(jwt)
                .userId(user.getId())
                .build();
    }

    public static UserResponseDto.SocialInfoDto toSocialInfoDto(User user){
        return UserResponseDto.SocialInfoDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .build();
    }

    public static UserResponseDto.UserExerciseDto toUserExerciseDto(UserExercise userExercise){
        return UserResponseDto.UserExerciseDto.builder()
                .GradeName(userExercise.getGrade().getName())
                .category(userExercise.getExerciseCategory().getName())
                .maxExp(userExercise.getGrade().getMaxExp())
                .exp(userExercise.getExp())
                .level(userExercise.getGrade().getLevel())
                .build();
    }

    public static UserResponseDto.MyPageDto toMyPageDto(User user, List<UserExercise> myExerciseList){

        List<UserResponseDto.UserExerciseDto> userExerciseDtoList = myExerciseList.stream()
                .map(userExercise -> toUserExerciseDto(userExercise))
                .collect(Collectors.toList());


        return UserResponseDto.MyPageDto.builder()
                .myInfo(toCommunityUserInfo(user))
                .myExerciseList(userExerciseDtoList)
                .build();
    }

    public static UserResponseDto.MainExerciseChangeDto toMainExerciseChangeDto(UserExercise exercise){
        return UserResponseDto.MainExerciseChangeDto.builder()
                .mainExerciseName(exercise.getExerciseCategory().getName())
                .build();
    }

    public static UserResponseDto.ReportUserDto toReportUserDto(Long userId, UserReport userReport){
        return UserResponseDto.ReportUserDto.builder()
                .ReportedUserId(userId)
                .reportedAt(LocalDateTime.now())
                .build();
    }

    public static UserResponseDto.OtherUserProfileDto toOtherUserProfileDto(User user){
        return UserResponseDto.OtherUserProfileDto.builder()
                .profileUrl(user.getProfileUrl())
                .nickname(user.getNickname())
                .mainExerciseInfo(UserExerciseConverter.toUserExerciseDto(user))
                .build();
    }

    public static UserResponseDto.ChangeMyProfileDto toChangeMyProfileDto(String imageUrl){
        return UserResponseDto.ChangeMyProfileDto.builder()
                .changedImageUrl(imageUrl)
                .changedAt(LocalDateTime.now())
                .build();
    }

    public static UserResponseDto.CurrentMainExerciseDto toCurrentMainExerciseDto(User user){
        return UserResponseDto.CurrentMainExerciseDto.builder()
                .currentExerciseCategory(user.getMainExercise().getExerciseCategory().getId())
                .build();
    }
}
