package fithub.app.service;

import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    Article create(ArticleRequestDto.CreateArticleDto request, User user, Integer categoryId)throws IOException;

    Article getArticle(Long ArticleId);

    Boolean getIsSaved(Article article, User user);

    Boolean getIsLiked(Article article, User user);

    Article toggleArticleLike(Long articleId, User user);

    Article toggleArticleSave(Long articleId, User user);

    Article updateArticle(Long articleId, ArticleRequestDto.UpdateArticleDto request, User user)throws IOException;

    void deleteArticleSingle(Long articleId, User user);

    Page<Article> findArticlePagingCategoryAndCreatedAt(User user, Integer categoryId, Long last);
    Page<Article> findArticlePagingCreatedAt(User user, Long last);

    Page<Article> findArticlePagingCategoryAndLikes(User user, Integer categoryId, Long last);
    Page<Article> findArticlePagingLikes(User user, Long last);
}
