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

    public static class OAuthUserInfoDto{
        Boolean marketingAgree;
        String name;
        String phoneNum;
        String nickname;
        String birth;
        Integer gender;
        List<Integer> preferExercises;
    }

    public static class UserInfoDto{
        Boolean marketingAgree;
        String password;
        String name;
        String phoneNum;
        String nickname;
        String birth;
        Integer gender;
        List<Integer> preferExercises;
    }
}
