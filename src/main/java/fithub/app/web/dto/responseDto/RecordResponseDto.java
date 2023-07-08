package fithub.app.web.dto.responseDto;

import lombok.*;

import java.time.LocalDateTime;

public class RecordResponseDto {


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecordSpecDto{
        Long recordId;
        ExerciseCategoryResponseDto.CategoryDto recordCategory;
        UserResponseDto.ArticleUserDto userInfo;
        String title;
        String contents;
        PictureResponseDto.PictureDto recordPicture;
        LocalDateTime createdAt;
        HashTagResponseDto.HashtagDto Hashtags;
        Boolean isLiked;
        Boolean isScraped;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordDto{
        Long recordId;
        ExerciseCategoryResponseDto.CategoryDto recordCategory;
        String title;
        String contents;
        Long comments;
        Long views;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordDtoList{
        recordDto[] recordList;
        Long size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordCreateDto{
        Long recordId;
        String title;
        Long ownerId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordUpdateDto{
        Long recordId;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordDeleteDto{
        Long recordId;
        LocalDateTime deletedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordDeleteDtoList{
         recordDeleteDto[] deletedRecordList;
        Long size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordLikeDto{
        Long recordId;
        Long userId;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordScrapDto{
        Long recordId;
        Long userId;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordPictureUploadDto{
        Long articleId;
        String pictureUrl;
    }
}
