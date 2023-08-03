package fithub.app.converter;

import fithub.app.domain.*;
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

    public static RootApiResponseDto.MyLevelDto toMyLevelDto(User user){
        Grade grade = user.getMainExercise().getGrade();
        return RootApiResponseDto.MyLevelDto.builder()
                .levelIconUrl(grade.getGradeIcon())
                .level(grade.getLevel())
                .levelName(grade.getName())
                .levelSummary(grade.getSummary())
                .levelDescription(grade.getDescription())
                .build();
    }

    public static RootApiResponseDto.LevelDto toLevelDto(Grade grade){
        return RootApiResponseDto.LevelDto.builder()
                .levelIconUrl(grade.getGradeIcon())
                .level(grade.getLevel())
                .levelName(grade.getName())
                .build();

    }

    public static RootApiResponseDto.FithubLevelInfoDto toFithubLevelInfoDto(List<Grade> gradeList, LevelInfo levelInfo){
        List<RootApiResponseDto.LevelDto> levelDtoList = gradeList.stream()
                .map(grade -> toLevelDto(grade))
                .collect(Collectors.toList());

        return RootApiResponseDto.FithubLevelInfoDto.builder()
                .FithubLevelList(levelDtoList)
                .expSummary(levelInfo.getExpSummary())
                .expDescription(levelInfo.getExpInfo())
                .comboSummary(levelInfo.getComboSummary())
                .comboDescription(levelInfo.getComboExpInfo())
                .build();
    }

    public static RootApiResponseDto.LevelInfoDto toLevelInfoDto(User user, List<Grade> gradeList, LevelInfo levelInfo){
        return RootApiResponseDto.LevelInfoDto.builder()
                .myLevelInfo(toMyLevelDto(user))
                .FithubLevelInfo(toFithubLevelInfoDto(gradeList, levelInfo))
                .build();
    }
}
