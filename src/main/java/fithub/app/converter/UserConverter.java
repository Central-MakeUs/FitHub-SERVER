package fithub.app.converter;

import fithub.app.auth.provider.TokenProvider;
import fithub.app.base.Code;
import fithub.app.domain.User;
import fithub.app.domain.enums.Gender;
import fithub.app.domain.enums.SocialType;
import fithub.app.base.exception.handler.UserException;
import fithub.app.repository.UserRepository;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.requestDto.UserRequestDto;
import fithub.app.web.dto.responseDto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final UserRepository userRepository;

    private static UserRepository staticUserRepository;

    private final TokenProvider tokenProvider;

    private static TokenProvider staticTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private static PasswordEncoder staticPasswordEncoder;

    @PostConstruct
    public void init() {
        this.staticUserRepository = this.userRepository;
        this.staticPasswordEncoder = this.passwordEncoder;
        this.staticTokenProvider = this.tokenProvider;
    }

    public static User toCreateOAuthUser(String socialId, SocialType socialType){
        return User.builder()
                .socialId(socialId)
                .socialType(socialType)
                .isSocial(true)
                .build();
    }

    public static User toUserPhoneNum(UserRequestDto.UserInfo request){

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

        return User.builder()
                .marketingAgree(request.getMarketingAgree())
                .phoneNum(request.getPhoneNumber())
                .name(request.getName())
                .nickname(request.getNickname())
                .password(staticPasswordEncoder.encode(request.getPassword()))
                .age(age)
                .gender(gender)
                .build();

    }

    public static User toSocialUser(UserRequestDto.UserOAuthInfo request, User user){
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

        return user.updateInfo(request, age,gender);
    }

    public static User toUser(Long userId){
        return staticUserRepository.findById(userId).orElseThrow(()->new UserException(Code.MEMBER_NOT_FOUND));
    }

    public static UserResponseDto.OauthDto toOauthDto(OAuthResult.OAuthResultDto result){
        return UserResponseDto.OauthDto.builder()
                .accessToken(result.getJwt())
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
}
