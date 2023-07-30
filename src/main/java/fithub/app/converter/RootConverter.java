package fithub.app.converter;

import fithub.app.domain.BestRecorder;
import fithub.app.domain.User;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RootConverter {


    public static RootApiResponseDto.AutoLoginResponseDto toAutoLoginResponseDto(Long userId, String accessToken){
        return RootApiResponseDto.AutoLoginResponseDto.builder()
                .userId(userId)
                .accessToken(accessToken)
                .build();
    }

//    public static RootApiResponseDto.BestRecorderDto toBestRecorderDto(BestRecorder bestRecorder){
//        return RootApiResponseDto.BestRecorderDto.builder()
//                .category(bestRecorder.getUser().get)
//                .build()
//    }
//
//    public static RootApiResponseDto.HomeProfileDto toHomeProfileDto(User user, List<BestRecorder> bestRecorderList){
//
//    }
}
