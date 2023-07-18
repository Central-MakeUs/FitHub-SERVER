package fithub.app.service.impl;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.ArticleException;
import fithub.app.converter.ArticleConverter;
import fithub.app.converter.HashTagConverter;
import fithub.app.domain.Article;
import fithub.app.domain.HashTag;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ArticleLikes;
import fithub.app.domain.mapping.SavedArticle;
import fithub.app.repository.ArticleRepositories.ArticleLikesRepository;
import fithub.app.repository.ArticleRepositories.ArticleRepository;
import fithub.app.repository.ArticleRepositories.SavedArticleRepository;
import fithub.app.repository.HashTagRepository;
import fithub.app.service.ArticleService;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final HashTagRepository hashTagRepository;

    private final ArticleLikesRepository articleLikesRepository;

    private final SavedArticleRepository savedArticleRepository;

    @Override
    @Transactional(readOnly = false)
    public Article create(ArticleRequestDto.CreateArticleDto request, User user, Integer categoryId) throws IOException
    {
//
        List<HashTag> hashTagList = request.getTagList().stream()
                .map(tag -> hashTagRepository.findByName(tag).orElseGet(()-> HashTagConverter.newHashTag(tag)))
                .collect(Collectors.toList());

        System.out.println(hashTagList.size());
        Article article = ArticleConverter.toArticle(request, user, hashTagList, categoryId);
        return articleRepository.save(article);
    }

    @Override
    public Article getArticle(Long ArticleId) {
        return articleRepository.findById(ArticleId).orElseThrow(()-> new ArticleException(Code.ARTICLE_NOT_FOUND));
    }

    @Override
    public Boolean getIsSaved(Article article, User user){
        Optional<SavedArticle> isSaved = savedArticleRepository.findByArticleAndUser(article, user);
        return isSaved.isPresent();
    }

    @Override
    public Boolean getIsLiked(Article article, User user){
        Optional<ArticleLikes> isLiked = articleLikesRepository.findByArticleAndUser(article, user);
        return isLiked.isPresent();
    }

    @Override
    @Transactional(readOnly = false)
    public Article toggleArticleLike(Long articleId, User user) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(Code.ARTICLE_NOT_FOUND));
        Optional<ArticleLikes> articleLike = articleLikesRepository.findByArticleAndUser(article, user);

        Article updatedArticle;

        if(articleLike.isPresent()){
            articleLike.get().getUser().getArticleLikesList().remove(articleLike.get());
            articleLikesRepository.delete(articleLike.get());
            updatedArticle = article.likeToggle(false);
        }else{
            updatedArticle = article.likeToggle(true);
            ArticleLikes articleLikes = ArticleLikes.builder()
                    .article(updatedArticle)
                    .user(user)
                    .build();
            articleLikes.setUser(user);
            articleLikesRepository.save(articleLikes);
        }

        return updatedArticle;
    }
}
