package fithub.app.web.dto;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {


    @Getter @Setter
    public static class socialDto{
        private String socialId;
    }

    @Getter @Setter
    public static class AppleSocialDto{
        private String identityToken;
    }
}
