package fithub.app.web.dto.responseDto;

import fithub.app.domain.User;
import lombok.*;

import java.time.LocalDateTime;

public class ArticleResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleUserDto{
        Long ownerId;
        String ProfileUrl;
        String nickname;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleHashtagDto{
        Long hashTagId;
        String name;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleCategoryDto{
        Integer categoryId;
        String name;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticlePictureDto{
        Long pictureId;
        String PictureUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticlePictureDtoList{
        ArticlePictureDto[] articlePictureDtoList;
        Long size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleSpecDto{
        Long articleId;
        ArticleCategoryDto articleCategoryDto;
        ArticleUserDto articleUserDto;
        String title;
        String contents;
        ArticlePictureDtoList articlePictureDtoList;
        LocalDateTime createdAt;
        ArticleHashtagDto articleHashtagDto;
        Boolean isLiked;
        Boolean isScraped;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleDto{
        Long articleId;
        ArticleCategoryDto articleCategoryDto;
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
        ArticleDto[] articleDtoList;
        Long size;
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
        ArticleDeleteDto[] articleDeleteDtoList;
        Long size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleLikeDto{
        Long articleId;
        Long userId;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleLikeCancleDto{
        Long articleId;
        Long userId;
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
    public static class ArticleScrapCancleDto{
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
