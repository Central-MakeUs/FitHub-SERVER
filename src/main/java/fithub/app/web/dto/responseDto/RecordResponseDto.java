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
        String loginUserProfileUrl;
        UserResponseDto.CommunityUserInfo userInfo;
        String contents;
        String pictureImage;
        Long comments;
        String createdAt;
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
        @Schema(description = "사진 url")
        String pictureUrl;
        @Schema(description = "좋아요 갯수")
        Long likes;
        @Schema(description = "작성 시간")
        String createdAt;

        Boolean isLiked;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class recordDtoList{
        List<recordDto> recordList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
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
        Boolean isLiked;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecordReportDto{
        Long reportedRecordId;
        LocalDateTime reportedAt;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecordLimitDto{
        Boolean isWrite;
    }
}
