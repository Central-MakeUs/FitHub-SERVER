package fithub.app.web.dto.responseDto;

import fithub.app.domain.ExerciseCategory;
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
        private Long id;
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
        private Boolean isSocial;
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

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FacilitiesInfoDto {
        private  String name;
        private  String address;
        private  String roadAddress;
        private  String imageUrl;
        private  String phoneNumber;
        private  String category;
        private  Integer categoryId;
        private  String x;
        private  String y;
        private  String dist;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FacilitiesInfoKeywordDto {
        private  String name;
        private  String address;
        private  String roadAddress;
        private  String imageUrl;
        private  String phoneNumber;
        private  String category;
        private  String x;
        private  String y;
        private  String centerX;
        private  String centerY;
        private  String dist;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FacilitiesResponseDto{
        List<FacilitiesInfoDto> facilitiesList;
        private Integer size;
        private String userX;
        private String userY;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FacilitiesResponseKeywordDto{
        List<FacilitiesInfoKeywordDto> facilitiesList;
        private Integer size;
        private String userX;
        private String userY;
        private String centerX;
        private String centerY;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveAsImageUrlDto{
        String SavedImageUrl;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NotificationPermitDto{
        Boolean communityPermit;
        Boolean marketingPermit;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NotificationChangeDto{
        Boolean CommunityPermit;
        Boolean MarketingPermit;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FacilitiesKeywordRecommendDto{
        List<String> keywordList;
        Integer size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TermsListDto{
        List<TermsDto> termsDtoList;
        Integer size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TermsDto{
        Integer id;
        private String title;
        String link;
    }
}
