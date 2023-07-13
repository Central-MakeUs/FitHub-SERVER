package fithub.app.web.dto.common;

import lombok.*;

public class BaseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BaseResponseDto{
        @Override
        public String toString() {
            return "BaseResponseDto{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", result=" + result +
                    '}';
        }

        private Integer code;
        private String message;
        private Object result;
    }
}
