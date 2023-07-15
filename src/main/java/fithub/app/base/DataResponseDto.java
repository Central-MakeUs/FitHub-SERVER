package fithub.app.base;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DataResponseDto<T> extends ResponseDto {


    private DataResponseDto(T result) {
        super(true, Code.OK.getCode(), Code.OK.getMessage(), result);
    }

    private DataResponseDto(T result, String message) {
        super(true, Code.OK.getCode(), message, result);
    }

    private DataResponseDto(T result, Code code){
        super(true, code.getCode(), code.getMessage(), result);
    }


    public static <T> DataResponseDto<T> of(T result) {
        return new DataResponseDto<>(result);
    }

    public static <T> DataResponseDto<T> of(T result, String message) {
        return new DataResponseDto<>(result, message);
    }

    public static <T> DataResponseDto<T> of(T result, Code code){
        return new DataResponseDto<>(result, code);
    }

    public static <T> DataResponseDto<T> empty() {
        return new DataResponseDto<>(null);
    }
}
