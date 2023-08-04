package fithub.app.web.dto.responseDto;

import fithub.app.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.java.Log;

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
        String loginUserProfileUrl;
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
        Long articleId;
        UserResponseDto.CommunityUserInfo userInfo;
        ExerciseCategoryResponseDto.CategoryDto articleCategory;
        String title;
        String contents;
        String pictureUrl;
        String exerciseTag;
        Long likes;
        Long comments;
        Boolean isLiked;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleDtoList{
        List<ArticleDto> articleList;
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
        Boolean isLiked;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleSaveDto{
        Long articleId;
        Long articleSaves;
        Boolean isSaved;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticlePictureUploadDto{
        Long articleId;
        String pictureUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleReportDto{
        Long reportedArticleId;
        LocalDateTime reportedAt;
    }
}
