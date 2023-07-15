package fithub.app.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class ResponseDto<T> {

    private final Boolean isSuccess;
    private final Integer code;
    private final String message;
    private final T result;

    public static ResponseDto of(Boolean isSuccess, Code code) {
        return new ResponseDto(isSuccess, code.getCode(), code.getMessage(), null);
    }

    public static ResponseDto of(Boolean isSuccess, Code errorCode, Exception e) {
        return new ResponseDto(isSuccess, errorCode.getCode(), errorCode.getMessage(e),null);
    }

    public static ResponseDto of(Boolean isSuccess, Code errorCode, String message) {
        return new ResponseDto(isSuccess, errorCode.getCode(), errorCode.getMessage(message),null);
    }
}
