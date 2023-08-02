package fithub.app.converter;

import fithub.app.domain.BestRecorder;
import fithub.app.domain.Grade;
import fithub.app.domain.User;
import fithub.app.domain.UserExercise;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RootConverter {


    public static RootApiResponseDto.AutoLoginResponseDto toAutoLoginResponseDto(Long userId, String accessToken){
        return RootApiResponseDto.AutoLoginResponseDto.builder()
                .userId(userId)
                .accessToken(accessToken)
                .build();
    }

    public static RootApiResponseDto.BestRecorderDto toBestRecorderDto(BestRecorder bestRecorder){
        UserExercise bestRecordExercise = bestRecorder.getUser().getBestRecordExercise();
        return RootApiResponseDto.BestRecorderDto.builder()
                .ranking(bestRecorder.getRanking())
                .rankingStatus(bestRecorder.getRankingStatus().toString())
                .recorderNickName(bestRecorder.getUser().getNickname())
                .category(bestRecordExercise.getExerciseCategory().getName())
                .GradeName(bestRecordExercise.getGrade().getName())
                .level(bestRecordExercise.getGrade().getLevel())
                .profileUrl(bestRecorder.getUser().getProfileUrl())
                .recordCount(bestRecordExercise.getRecords())
                .build();
    }

    public static RootApiResponseDto.HomeUserInfo toHomeUserInfo(User user){
        Grade mainGrade = user.getMainExercise().getGrade();
        return RootApiResponseDto.HomeUserInfo.builder()
                .userNickname(user.getNickname())
                .GradeName(mainGrade.getName())
                .GradeImageUrl(mainGrade.getGradeIcon())
                .category(user.getMainExercise().getExerciseCategory().getName())
                .exp(user.getMainExercise().getExp())
                .maxExp(mainGrade.getMaxExp())
                .monthRecordCount(user.getMonthlyRecordNum())
                .contiguousRecordCount(user.getContiguousRecordNum())
                .build();
    }

    public static RootApiResponseDto.HomeProfileDto toHomeProfileDto(User user, List<BestRecorder> bestRecorderList){
        List<RootApiResponseDto.BestRecorderDto> bestRecorderDtoList = bestRecorderList.stream()
                .map(bestRecorder -> toBestRecorderDto(bestRecorder))
                .collect(Collectors.toList());

        return RootApiResponseDto.HomeProfileDto.builder()
                .bestRecorderList(bestRecorderDtoList)
                .userInfo(toHomeUserInfo(user))
                .BestStandardDate(bestRecorderList.get(0).getStandardDate())
                .build();
    }
}
