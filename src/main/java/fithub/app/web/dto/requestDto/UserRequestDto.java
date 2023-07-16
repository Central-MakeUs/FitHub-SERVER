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
    public class FindPassDto{
        @Override
        public String toString() {
            return "FindPassDto{" +
                    "targetPhoneNum='" + targetPhoneNum + '\'' +
                    '}';
        }

        private String targetPhoneNum;
    }

    @Getter @Setter
    public class ChangePassDto{
        @Override
        public String toString() {
            return "ChangePassDto{" +
                    "newPassword='" + newPassword + '\'' +
                    '}';
        }

        private String newPassword;
    }
}
