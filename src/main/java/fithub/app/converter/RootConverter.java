package fithub.app.converter;

import fithub.app.utils.ResponseCode;
import fithub.app.web.dto.RootApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class RootConverter {

    public static RootApiResponseDto.AutoLoginDto toAutoLoginDto(ResponseCode result){
        return RootApiResponseDto.AutoLoginDto.builder()
                .code(result.getCode())
                .message(result.getMessage())
                .build();
    }
}
