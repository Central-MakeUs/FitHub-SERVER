package fithub.app.utils;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginResult {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginResultDto{
        String jwt;
        String refreshToken;
    }
}
