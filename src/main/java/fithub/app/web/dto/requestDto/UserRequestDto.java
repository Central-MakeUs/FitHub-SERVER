package fithub.app.web.dto.requestDto;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class UserRequestDto {


    @Getter @Setter
    public static class socialDto{
        private String socialId;
    }

    @Getter @Setter
    public static class AppleSocialDto{
        @Override
        public String toString() {
            return "AppleSocialDto{" +
                    "identityToken='" + identityToken + '\'' +
                    '}';
        }

        private String identityToken;
    }

    @Getter @Setter
    public static class UserOAuthInfo{
        @Override
        public String toString() {
            return "UserOAuthInfo{" +
                    "marketingAgree=" + marketingAgree +
                    ", name='" + name + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", birth='" + birth + '\'' +
                    ", gender='" + gender + '\'' +
                    ", preferExercises=" + preferExercises +
                    '}';
        }

        private Boolean marketingAgree;
        @NotBlank
        private String name;
        @NotBlank
        private String nickname;
        @NotBlank
        private String birth;
        @NotBlank
        private String gender;
        private List<Integer> preferExercises;
    }

    @Getter @Setter
    public static class UserInfo{

        private Boolean marketingAgree;
        private String phoneNumber;
        private String name;
        private String nickname;
        private String password;
        private String birth;
        private String gender;
        private List<Integer> preferExercises;
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

    @Getter @Setter
    public static class FindPassDto{
        @Override
        public String toString() {
            return "FindPassDto{" +
                    "targetPhoneNum='" + targetPhoneNum + '\'' +
                    '}';
        }

        private String targetPhoneNum;
    }

    @Getter @Setter
    public static class ChangePassDto{
        @Override
        public String toString() {
            return "ChangePassDto{" +
                    "targetPhoneNum='" + targetPhoneNum + '\'' +
                    ", newPassword='" + newPassword + '\'' +
                    '}';
        }

        private String targetPhoneNum;
        private String newPassword;
    }

    @Getter @Setter
    public static class LoginDto{
        private String targetPhoneNum;
        private String password;
    }
}
