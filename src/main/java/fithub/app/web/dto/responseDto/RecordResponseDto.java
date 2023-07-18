package fithub.app.web.dto.responseDto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
        UserResponseDto.CommunityUserInfo userInfo;
        String contents;
        String pictureImage;
        LocalDateTime createdAt;
        HashTagResponseDto.HashtagDtoList Hashtags;
        Long likes;
        Boolean isLiked;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordDto{
        @Schema(description = "인증 글의 아이디")
        Long recordId;
        UserResponseDto.CommunityUserInfo userInfo;
        @Schema(description = "사진 url")
        String pictureUrl;
        @Schema(description = "운동 태그")
        String exerciseTag;
        @Schema(description = "좋아요 갯수")
        Long likes;
        @Schema(description = "댓글 갯수")
        Long comments;
        @Schema(description = "작성 시간")
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
        Long newLikes;
    }
}
