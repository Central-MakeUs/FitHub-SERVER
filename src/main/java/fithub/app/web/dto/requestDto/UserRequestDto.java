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
        @Override
        public String toString() {
            return "UserOAuthInfo{" +
                    "marketingAgree=" + marketingAgree +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", name='" + name + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", birthNum='" + birthNum + '\'' +
                    ", preferExercises=" + preferExercises +
                    '}';
        }

        private Boolean marketingAgree;
        private String phoneNumber;
        private String name;
        private String nickname;
        private String birthNum;
        private List<Long> preferExercises;
    }

    @Getter @Setter
    public static class UserInfo{
        @Override
        public String toString() {
            return "UserInfo{" +
                    "marketingAgree=" + marketingAgree +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", name='" + name + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", password='" + password + '\'' +
                    ", birthNum='" + birthNum + '\'' +
                    ", preferExercises=" + preferExercises +
                    '}';
        }

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

        @Override
        public String toString() {
            return "SmsRequestDto{" +
                    "targetPhoneNum='" + targetPhoneNum + '\'' +
                    '}';
        }
    }

    @Getter @Setter
    public static class PhoneNumAuthDto{
        @Override
        public String toString() {
            return "PhoneNumAuthDto{" +
                    "phoneNum='" + phoneNum + '\'' +
                    ", authNum=" + authNum +
                    '}';
        }

        private String phoneNum;
        private Integer authNum;
    }
}
