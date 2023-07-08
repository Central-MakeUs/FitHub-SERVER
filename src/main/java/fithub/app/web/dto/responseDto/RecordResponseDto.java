package fithub.app.web.dto.responseDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class RecordResponseDto {


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecordSpecDto{
        Long recordId;
        ExerciseCategoryResponseDto.CategoryDto recordCategory;
        UserResponseDto.ArticleUserDto userInfo;
        String contents;
        String pictureUrl;
        LocalDateTime createdAt;
        HashTagResponseDto.HashtagDtoList Hashtags;
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
        String pictureUrl;
        Long likes;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordDtoList{
        List<recordDto> recordList;
        Integer size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordCreateDto{
        Long recordId;
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
        List<recordDeleteDto> deletedRecordList;
        Integer size;
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
