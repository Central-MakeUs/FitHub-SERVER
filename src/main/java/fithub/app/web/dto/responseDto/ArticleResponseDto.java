package fithub.app.web.dto.responseDto;

import fithub.app.domain.User;
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
        UserResponseDto.ArticleUserDto userInfo;
        String title;
        String contents;
        PictureResponseDto.PictureDtoList articlePictureList;
        LocalDateTime createdAt;
        HashTagResponseDto.HashtagDtoList Hashtags;
        Long likes;
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
        ExerciseCategoryResponseDto.CategoryDto articleCategory;
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
        Long ArticleLikes;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleScrapDto{
        Long articleId;
        Long userId;
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
