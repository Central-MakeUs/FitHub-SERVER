package fithub.app.service.impl;

import fithub.app.converter.ArticleConverter;
import fithub.app.converter.HashTagConverter;
import fithub.app.domain.Article;
import fithub.app.domain.HashTag;
import fithub.app.domain.User;
import fithub.app.repository.ArticleRepository;
import fithub.app.repository.HashTagRepository;
import fithub.app.service.ArticleService;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final HashTagRepository hashTagRepository;

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
}
