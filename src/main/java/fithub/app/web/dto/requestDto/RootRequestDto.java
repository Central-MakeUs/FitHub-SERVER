package fithub.app.web.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

public class RootRequestDto {

    @Getter @Setter
    public static class AutoLoginDto{

        @Nullable
        String FCMToken;
    }
}
