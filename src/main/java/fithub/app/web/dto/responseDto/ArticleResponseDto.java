package fithub.app.web.dto.responseDto;

import fithub.app.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleSpecDto{
        Long articleId;
        ExerciseCategoryResponseDto.CategoryDto articleCategory;
        UserResponseDto.CommunityUserInfo userInfo;
        String title;
        String contents;
        PictureResponseDto.PictureDtoList articlePictureList;
        LocalDateTime createdAt;
        HashTagResponseDto.HashtagDtoList Hashtags;
        Long likes;
        Long comments;
        Long scraps;
        Boolean isLiked;
        Boolean isScraped;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleDto{
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
    public static class ArticleDtoList{
        List<ArticleDto> articleList;
        Integer size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleCreateDto{
        Long articleId;
        String title;
        Long ownerId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleUpdateDto{
        Long articleId;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleDeleteDto{
        Long articleId;
        LocalDateTime deletedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleDeleteDtoList{
        List<ArticleDeleteDto> deletedArticleList;
        Integer size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleLikeDto{
        Long articleId;
        Long articleLikes;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleSaveDto{
        Long articleId;
        Long articleSaves;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticlePictureUploadDto{
        Long articleId;
        String pictureUrl;
    }
}
