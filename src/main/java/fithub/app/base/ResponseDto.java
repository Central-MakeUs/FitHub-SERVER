package fithub.app.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class ResponseDto<T>{

    private final Boolean isSuccess;
    private final Integer code;
    private final String message;
    private final T result;

    public static<T> ResponseDto<T> of(Boolean isSuccess, Code code, T result) {
        return new ResponseDto<>(isSuccess, code.getCode(), code.getMessage(), result);
    }

    public static <T> ResponseDto<T> of(Code code, T result){
        return new ResponseDto<>(true, code.getCode(), code.getMessage(), result);
    }

    public static <T> ResponseDto<T> of(T result){
        return new ResponseDto<>(true, Code.OK.getCode(), Code.OK.getMessage(), result);
    }

    public static<T> ResponseDto<T> of(Boolean isSuccess, Code errorCode, Exception e, T result) {
        return new ResponseDto<>(isSuccess, errorCode.getCode(), errorCode.getMessage(e), result);
    }

    public static<T> ResponseDto<T> of(Boolean isSuccess, Code errorCode, String message, T result) {
        return new ResponseDto<>(isSuccess, errorCode.getCode(), errorCode.getMessage(message), result);
    }

    public static <T> ResponseDto<T> empty(){
        return new ResponseDto<>(true, Code.OK.getCode(), Code.OK.getMessage(), null);
    }
}
