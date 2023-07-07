package fithub.app.converter.common;

import fithub.app.utils.ResponseCode;
import fithub.app.web.dto.common.BaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseConverter {

    public static BaseDto.BaseResponseDto toBaseDto(ResponseCode responseCode, Object result){
        return BaseDto.BaseResponseDto.builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .result(result)
                .build();
    }
}
