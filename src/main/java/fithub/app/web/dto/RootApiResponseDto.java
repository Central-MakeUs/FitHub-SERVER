package fithub.app.web.dto;

import lombok.*;

public class RootApiResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AutoLoginDto{
        String code;
        String message;
    }
}
