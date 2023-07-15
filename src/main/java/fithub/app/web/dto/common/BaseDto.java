package fithub.app.web.dto.common;

import lombok.*;

public class BaseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BaseResponseDto{
        private Integer code;
        private String message;
    }
}
