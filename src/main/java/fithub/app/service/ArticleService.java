package fithub.app.service;

import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.web.dto.requestDto.ArticleRequestDto;

import java.io.IOException;

public interface ArticleService {

    Article create(ArticleRequestDto.CreateArticleDto request, User user, Integer categoryId)throws IOException;

    Article getArticle(Long ArticleId);

    Boolean getIsSaved(Article article, User user);

    Boolean getIsLiked(Article article, User user);

    Article toggleArticleLike(Long articleId, User user);

    Article toggleArticleSave(Long articleId, User user);

    Article updateArticle(Long articleId, ArticleRequestDto.UpdateArticleDto request, User user)throws IOException;
}
