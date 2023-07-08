package fithub.app.converter;

import fithub.app.domain.*;
import fithub.app.repository.ArticleRepository;
import fithub.app.web.dto.responseDto.ArticleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleConverter {

    private final ArticleRepository articleRepository;
    private static ArticleRepository staticArticleRepository;

    @PostConstruct
    public void init() {
        staticArticleRepository = this.articleRepository;
    }

    public static ArticleResponseDto.ArticleSpecDto toArticleSpecDto(Article article, List<ArticleImages> articleImages, List<HashTag> hashTagList,
                                                                      Boolean isLiked, Boolean isScraped){
        return ArticleResponseDto.ArticleSpecDto.builder()
                .articleId(article.getId())
                .articleCategory(ExerciseCategoryConverter.toCategoryDto(article.getExerciseCategory()))
                .userInfo(UserConverter.toArticleUserDto(article.getUser()))
                .title(article.getTitle())
                .contents(article.getContents())
                .articlePictureList(PictureConverter.toPictureDtoList(articleImages))
                .createdAt(article.getCreatedAt())
                .Hashtags(HashTagConverter.toHashtagDtoList(hashTagList))
                .isLiked(isLiked)
                .isScraped(isScraped)
                .build();
    }

    public static ArticleResponseDto.ArticleDto toArticleDto(Article article){
        return ArticleResponseDto.ArticleDto.builder()
                .articleId(article.getId())
                .articleCategory(ExerciseCategoryConverter.toCategoryDto(article.getExerciseCategory()))
                .title(article.getTitle())
                .comments(article.getComments())
                .views(article.getViews())
                .createdAt(article.getCreatedAt())
                .build();
    }

    ArticleResponseDto.ArticleDtoList toArticleDtoList(List<Article> articleList){
        List<ArticleResponseDto.ArticleDto> articleDtoList =
                articleList.stream()
                        .map(article -> toArticleDto(article))
                        .collect(Collectors.toList());

        return ArticleResponseDto.ArticleDtoList.builder()
                .articleList(articleDtoList)
                .size(articleDtoList.size())
                .build();
    }

    public ArticleResponseDto.ArticleCreateDto toArticleCreateDto(Article article){
        return ArticleResponseDto.ArticleCreateDto.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .ownerId(article.getUser().getId())
                .createdAt(article.getCreatedAt())
                .build();
    }

    public ArticleResponseDto.ArticleUpdateDto toArticleUpdateDto(Article article){
        return ArticleResponseDto.ArticleUpdateDto.builder()
                .articleId(article.getId())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    public ArticleResponseDto.ArticleDeleteDto toArticleDeleteDto(Long id){
        return ArticleResponseDto.ArticleDeleteDto.builder()
                .articleId(id)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public ArticleResponseDto.ArticleDeleteDtoList toArticleDeleteDtoList(List<Long> idList){

        List<ArticleResponseDto.ArticleDeleteDto> articleDeleteDtoList =
                idList.stream()
                        .map(id -> toArticleDeleteDto(id))
                        .collect(Collectors.toList());

        return ArticleResponseDto.ArticleDeleteDtoList.builder()
                .deletedArticleList(articleDeleteDtoList)
                .size(articleDeleteDtoList.size())
                .build();
    }

    public ArticleResponseDto.ArticleLikeDto toArticleLikeDto(Article article, User user){
        return ArticleResponseDto.ArticleLikeDto.builder()
                .articleId(article.getId())
                .userId(user.getId())
                .build();
    }

    public ArticleResponseDto.ArticleScrapDto toArticleScrapDto(Article article, User user){
        return ArticleResponseDto.ArticleScrapDto.builder()
                .articleId(article.getId())
                .userId(user.getId())
                .build();
    }
}
