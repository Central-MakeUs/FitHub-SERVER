package fithub.app.service;

import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ContentsReport;
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

    Page<Article> findArticlePagingCategoryAndCreatedAt(User user, Integer categoryId, Integer last);
    Page<Article> findArticlePagingCreatedAt(User user, Integer last);

    Page<Article> findArticlePagingCategoryAndLikes(User user, Integer categoryId, Integer last);
    Page<Article> findArticlePagingLikes(User user, Integer last);

    void deleteArticleBulk(ArticleRequestDto.DeleteListArticleDto request, User user);

    ContentsReport reportArticle(Long articleId, User user);

    void alarmArticleLike(Article article, User user);
}
