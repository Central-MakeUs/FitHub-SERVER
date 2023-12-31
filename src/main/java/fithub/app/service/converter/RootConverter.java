package fithub.app.service.converter;

import fithub.app.domain.*;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RootConverter {


    private final ExerciseCategoryRepository exerciseCategoryRepository;
    private static ExerciseCategoryRepository staticExerciseCategoryRepository;

    @PostConstruct
    public void init() {
        staticExerciseCategoryRepository = this.exerciseCategoryRepository;
    }

    public static RootApiResponseDto.AutoLoginResponseDto toAutoLoginResponseDto(Long userId, String accessToken){
        return RootApiResponseDto.AutoLoginResponseDto.builder()
                .userId(userId)
                .accessToken(accessToken)
                .build();
    }

    public static RootApiResponseDto.BestRecorderDto toBestRecorderDto(BestRecorder bestRecorder){
        return RootApiResponseDto.BestRecorderDto.builder()
                .id(bestRecorder.getUserId())
                .ranking(bestRecorder.getRanking())
                .rankingStatus(bestRecorder.getRankingStatus().toString())
                .recorderNickName(bestRecorder.getNickname())
                .category(bestRecorder.getExerciseName())
                .GradeName(bestRecorder.getGradeName())
                .level(bestRecorder.getLevel())
                .profileUrl(bestRecorder.getProfileUrl())
                .recordCount(bestRecorder.getRecordCount())
                .build();
    }

    public static RootApiResponseDto.HomeUserInfo toHomeUserInfo(User user){
        Grade mainGrade = user.getMainExercise().getGrade();
        return RootApiResponseDto.HomeUserInfo.builder()
                .userNickname(user.getNickname())
                .GradeName(mainGrade.getName())
                .isSocial(user.getIsSocial())
                .GradeImageUrl(mainGrade.getGradeIcon())
                .category(user.getMainExercise().getExerciseCategory().getName())
                .exp(user.getMainExercise().getExp())
                .maxExp(mainGrade.getMaxExp())
                .monthRecordCount(user.getMainExercise().getRecords())
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
                .BestStandardDate(bestRecorderList == null || bestRecorderList.size() == 0 ? LocalDate.now() : bestRecorderList.get(0).getStandardDate())
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

    public static RootApiResponseDto.SaveFacilitiesDto toSaveFacilitiesDto(Integer result){
        return RootApiResponseDto.SaveFacilitiesDto.builder().savedFacilities(result).build();
    }

    public static RootApiResponseDto.FacilitiesInfoDto toFacilitiesInfoDto(Object[] facilities){

        ExerciseCategory exerciseCategory = staticExerciseCategoryRepository.findById((Integer) facilities[7]).get();

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        DecimalFormat decimalFormat2 = new DecimalFormat("#");
        Double distance = (Double) facilities[8];
        String dist = distance >= 1000 ? decimalFormat.format(distance / 1000) + "km" : decimalFormat2.format(distance) + 'm';

        return RootApiResponseDto.FacilitiesInfoDto.builder()
                .name(((String) facilities[0]))
                .address((String) facilities[1])
                .roadAddress(((String) facilities[2]))
                .imageUrl((String) facilities[3])
                .phoneNumber((String) facilities[4])
                .x((String) facilities[5])
                .y((String) facilities[6])
                .category(exerciseCategory.getName())
                .categoryId((Integer) facilities[7])
                .dist(dist)
                .build();
    }

    public static RootApiResponseDto.FacilitiesInfoKeywordDto toFacilitiesInfoKeywordDto(Object[] facilities){

        ExerciseCategory exerciseCategory = staticExerciseCategoryRepository.findById((Integer) facilities[7]).get();

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        DecimalFormat decimalFormat2 = new DecimalFormat("#");
        Double distance = (Double) facilities[8];
        String dist = distance >= 1000 ? decimalFormat.format(distance / 1000) + "km" : decimalFormat2.format(distance) + 'm';

        return RootApiResponseDto.FacilitiesInfoKeywordDto.builder()
                .name(((String) facilities[0]))
                .address((String) facilities[1])
                .roadAddress(((String) facilities[2]))
                .imageUrl((String) facilities[3])
                .phoneNumber((String) facilities[4])
                .x((String) facilities[5])
                .y((String) facilities[6])
                .centerX((String) facilities[9])
                .centerY((String) facilities[10])
                .category(exerciseCategory.getName())
                .dist(dist)
                .build();
    }

    public static RootApiResponseDto.FacilitiesResponseDto toFacilitiesResponseDto(List<RootApiResponseDto.FacilitiesInfoDto> facilitiesList, String x, String y){

        return RootApiResponseDto.FacilitiesResponseDto.builder()
                .userX(x)
                .userY(y)
                .facilitiesList(facilitiesList)
                .size(facilitiesList.size())
                .build();
    }

    public static RootApiResponseDto.FacilitiesResponseKeywordDto toFacilitiesKeywordResponseDto(List<RootApiResponseDto.FacilitiesInfoKeywordDto> facilitiesList, String x, String y){

        return RootApiResponseDto.FacilitiesResponseKeywordDto.builder()
                .userX(x)
                .userY(y)
                .centerX(facilitiesList.size() > 1 ? facilitiesList.get(0).getCenterX() : null)
                .centerY(facilitiesList.size() > 1 ? facilitiesList.get(0).getCenterY() : null)
                .facilitiesList(facilitiesList)
                .size(facilitiesList.size())
                .build();
    }



    public static RootApiResponseDto.SaveAsImageUrlDto toSaveAsImageUrlDto(String s){
        return RootApiResponseDto.SaveAsImageUrlDto.builder()
                .SavedImageUrl(s)
                .build();
    }

    public static RootApiResponseDto.NotificationPermitDto toNotificationPermitDto(User user){
        return RootApiResponseDto.NotificationPermitDto.builder()
                .marketingPermit(user.getMarketingAgree())
                .communityPermit(user.getCommunityPermit())
                .build();
    }

    public static RootApiResponseDto.NotificationChangeDto toNotificationChangeDto(User user){
        return RootApiResponseDto.NotificationChangeDto.builder()
                .CommunityPermit(user.getCommunityPermit())
                .MarketingPermit(user.getMarketingAgree())
                .build();
    }

    public static RootApiResponseDto.FacilitiesKeywordRecommendDto toFacilitiesKeywordRecommendDto(List<RecommendFacilitiesKeyword> list){
        List<String> stringList = list.stream()
                .map(keyword -> keyword.getKeyword()).collect(Collectors.toList());

        return RootApiResponseDto.FacilitiesKeywordRecommendDto.builder()
                .keywordList(stringList)
                .size(stringList.size())
                .build();
    }

    public static RootApiResponseDto.TermsDto toTermsDto(Terms terms){
        return RootApiResponseDto.TermsDto.builder()
                .link(terms.getLink())
                .title(terms.getTitle())
                .id(terms.getId() - 1)
                .build();
    }

    public static RootApiResponseDto.TermsDto toTermsOneDto(Terms terms){
        return RootApiResponseDto.TermsDto.builder()
                .link(terms.getLink())
                .title(terms.getTitle())
                .id(terms.getId())
                .build();
    }

    public static RootApiResponseDto.TermsListDto toTermsResponseDto(List<Terms> termsList){
        List<RootApiResponseDto.TermsDto> termsDtoList = termsList.stream().map(
                terms -> toTermsDto(terms)
        ).collect(Collectors.toList());

        return RootApiResponseDto.TermsListDto.builder()
                .termsDtoList(termsDtoList)
                .size(termsList.size())
                .build();
    }
}
