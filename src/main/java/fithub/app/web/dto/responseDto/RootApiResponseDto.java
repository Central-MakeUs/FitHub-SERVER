package fithub.app.web.dto.responseDto;

import lombok.*;

public class RootApiResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AutoLoginResponseDto{
        Long userId;
        String accessToken;
    }
}
