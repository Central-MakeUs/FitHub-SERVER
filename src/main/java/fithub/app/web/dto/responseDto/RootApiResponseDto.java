package fithub.app.web.dto.responseDto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class RootApiResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AutoLoginResponseDto{
        Long userId;
        String accessToken;
    }
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BestRecorderDto{
        private Integer ranking;
        private String rankingStatus;
        private String recorderNickName;
        private String category;
        private String GradeName;
        private Integer level;
        private String profileUrl;
        private Long recordCount;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HomeUserInfo{
        private String userNickname;
        private String GradeName;
        private String GradeImageUrl;
        private String category;
        private Integer exp;
        private Integer maxExp;
        private Long monthRecordCount;
        private Long contiguousRecordCount;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HomeProfileDto{
        HomeUserInfo userInfo;
        List<BestRecorderDto> bestRecorderList;
        LocalDate BestStandardDate;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyLevelDto{
        String levelIconUrl;
        Integer level;
        String levelName;
        String levelSummary;
        String levelDescription;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LevelDto{
        String levelIconUrl;
        Integer level;
        String levelName;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FithubLevelInfoDto{
        List<LevelDto> FithubLevelList;
        String expSummary;
        String expDescription;
        String comboSummary;
        String comboDescription;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LevelInfoDto{
        MyLevelDto myLevelInfo;
        FithubLevelInfoDto FithubLevelInfo;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveFacilitiesDto{
        private Integer savedFacilities;
    }
}
