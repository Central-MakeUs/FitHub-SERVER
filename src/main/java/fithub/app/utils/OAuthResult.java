package fithub.app.utils;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthResult {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OAuthResultDto{
        Boolean isLogin;
        String jwt;
        Long userId;
    }
}
