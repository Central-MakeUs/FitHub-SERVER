package fithub.app.web.dto.requestDto;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserRequestDto {


    @Getter @Setter
    public static class socialDto{
        private String socialId;
    }

    @Getter @Setter
    public static class AppleSocialDto{
        private String identityToken;
    }

    @Getter @Setter
    public static class UserOAuthInfo{
        private Boolean marketingAgree;
        private String phoneNumber;
        private String name;
        private String nickname;
        private String birthNum;
        private List<Long> preferExercises;
    }

    @Getter @Setter
    public static class UserInfo{
        private Boolean marketingAgree;
        private String phoneNumber;
        private String name;
        private String nickname;
        private String password;
        private String birthNum;
        private List<Long> preferExercises;
    }

    @Getter @Setter
    public static class SmsRequestDto{
        private String targetPhoneNum;
    }

    @Getter @Setter
    public static class PhoneNumAuthDto{
        private String phoneNum;
        private Integer authNum;
    }
}
