package fithub.app.service;

import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.web.dto.requestDto.ArticleRequestDto;

import java.io.IOException;

public interface ArticleService {

    Article create(ArticleRequestDto.CreateArticleDto request, User user, Integer categoryId)throws IOException;
}
