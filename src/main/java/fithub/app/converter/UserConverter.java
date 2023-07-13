package fithub.app.converter;

import fithub.app.domain.User;
import fithub.app.domain.enums.Gender;
import fithub.app.domain.enums.SocialType;
import fithub.app.exception.common.ErrorCode;
import fithub.app.exception.handler.UserException;
import fithub.app.repository.UserRepository;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.requestDto.UserRequestDto;
import fithub.app.web.dto.responseDto.UserResponseDto;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final UserRepository userRepository;

    private static UserRepository staticUserRepository;

    private final PasswordEncoder passwordEncoder;

    private static PasswordEncoder staticPasswordEncoder;

    @PostConstruct
    public void init() {
        this.staticUserRepository = this.userRepository;
        this.staticPasswordEncoder = this.passwordEncoder;
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

    public static User toUser(Long userId){
        return staticUserRepository.findById(userId).orElseThrow(()->new UserException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public static UserResponseDto.OauthDto toOauthDto(OAuthResult.OAuthResultDto result){
        return UserResponseDto.OauthDto.builder()
                .accessToken(result.getJwt())
                .build();
    }

    public static UserResponseDto.ArticleUserDto toArticleUserDto(User user){
        return UserResponseDto.ArticleUserDto.builder()
                .ownerId(user.getId())
                .ProfileUrl(user.getProfileUrl())
                .nickname(user.getNickname())
                .build();
    }

    public static UserResponseDto.JoinUserDto toJoinUserDto(User user){
        return UserResponseDto.JoinUserDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .build();
    }
}
