package fithub.app.service.impl;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.SearchException;
import fithub.app.converter.SearchConverter;
import fithub.app.domain.Article;
import fithub.app.domain.HashTag;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ArticleHashTag;
import fithub.app.domain.mapping.RecordHashTag;
import fithub.app.repository.ArticleRepositories.ArticleRepository;
import fithub.app.repository.HashTagRepositories.ArticleHashTagRepository;
import fithub.app.repository.HashTagRepositories.HashTagRepository;
import fithub.app.repository.HashTagRepositories.RecordHashTagRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.service.SearchService;
import fithub.app.web.dto.responseDto.SearchPreViewResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final RecordRepository recordRepository;

    private final HashTagRepository hashTagRepository;

    private final ArticleRepository articleRepository;

    private final ArticleHashTagRepository articleHashTagRepository;

    private final RecordHashTagRepository recordHashTagRepository;

    private Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Value("${paging.size}")
    Integer size;

    @Override
    public Page<Article> searchArticleLikes(String tag, Long last) {
        Optional<HashTag> byName = hashTagRepository.findByName(tag);
        Page<Article> searchResult = null;
        if(byName.isEmpty())
            return searchResult;
        else{
            List<ArticleHashTag> allByHashTag = articleHashTagRepository.findAllByHashTag(byName.get());
            List<Long> articleIds = allByHashTag.stream()
                    .map(articleHashTag -> articleHashTag.getArticle().getId())
                    .collect(Collectors.toList());
            if(last == null)
                searchResult = articleRepository.findByIdInOrderByLikesDesc(articleIds, PageRequest.of(0, size));
            else
                searchResult = articleRepository.findByIdInAndLikesLessThanOrderByLikesDesc(articleIds, articleRepository.findById(last).get().getLikes(), PageRequest.of(0, size));
        }
        return searchResult;
    }

    @Override
    public Page<Record> searchRecordCreatedAt(String tag, Long last) {
        Optional<HashTag> byName = hashTagRepository.findByName(tag);
        Page<Record> searchResult = null;
        if(byName.isEmpty())
            return searchResult;
        else{
            List<RecordHashTag> allByHashTag = recordHashTagRepository.findAllByHashTag(byName.get());
            List<Long> recordIds = allByHashTag.stream()
                    .map(articleHashTag -> articleHashTag.getRecord().getId())
                    .collect(Collectors.toList());
            if(last == null)
                searchResult = recordRepository.findByIdInOrderByCreatedAtDesc(recordIds, PageRequest.of(0, size));
            else
                searchResult = recordRepository.findByIdInAndCreatedAtLessThanOrderByCreatedAtDesc(recordIds, recordRepository.findById(last).get().getCreatedAt(), PageRequest.of(0, size));
        }
        return searchResult;
    }
    @Override
    public Page<Article> searchArticleCreatedAt(String tag, Long last) {
        Optional<HashTag> byName = hashTagRepository.findByName(tag);
        Page<Article> searchResult = null;
        if(byName.isEmpty())
            return searchResult;
        else{
            List<ArticleHashTag> allByHashTag = articleHashTagRepository.findAllByHashTag(byName.get());
            List<Long> articleIds = allByHashTag.stream()
                    .map(articleHashTag -> articleHashTag.getArticle().getId())
                    .collect(Collectors.toList());
            if(last == null)
                searchResult = articleRepository.findByIdInOrderByCreatedAtDesc(articleIds, PageRequest.of(0, size));
            else
                searchResult = articleRepository.findByIdInAndCreatedAtLessThanOrderByCreatedAtDesc(articleIds, articleRepository.findById(last).get().getCreatedAt(), PageRequest.of(0, size));
        }
        return searchResult;
    }

    @Override
    public Page<Record> searchRecordLikes(String tag, Long last) {
        Optional<HashTag> byName = hashTagRepository.findByName(tag);
        Page<Record> searchResult = null;
        if(byName.isEmpty())
            return searchResult;
        else{
            List<RecordHashTag> allByHashTag = recordHashTagRepository.findAllByHashTag(byName.get());
            List<Long> recordIds = allByHashTag.stream()
                    .map(recordHashTag -> recordHashTag.getRecord().getId())
                    .collect(Collectors.toList());
            if(last == null)
                searchResult = recordRepository.findByIdInOrderByLikesDesc(recordIds, PageRequest.of(0, size));
            else
                searchResult = recordRepository.findByIdInAndLikesLessThanOrderByLikesDesc(recordIds, recordRepository.findById(last).get().getLikes(), PageRequest.of(0, size));
        }
        return searchResult;
    }

    @Override
    public SearchPreViewResponseDto.SearchPreViewDto searchPreview(String tag, User user) {
        Optional<HashTag> byName = hashTagRepository.findByName(tag);
        Page<Record> searchRecord = null;
        Page<Article> searchArticle = null;
        if(byName.isEmpty())
            throw new SearchException(Code.SEARCH_NO_DATA);
        else{
            List<RecordHashTag> recordHashTagList = recordHashTagRepository.findAllByHashTag(byName.get());
            List<Long> recordIds = recordHashTagList.stream()
                    .map(recordHashTag -> recordHashTag.getRecord().getId())
                    .collect(Collectors.toList());

            List<ArticleHashTag> articleHashTagList = articleHashTagRepository.findAllByHashTag(byName.get());
            List<Long> articleIds = articleHashTagList.stream()
                    .map(recordHashTag -> recordHashTag.getArticle().getId())
                    .collect(Collectors.toList());

            searchArticle = articleRepository.findByIdInOrderByCreatedAtDesc(articleIds,PageRequest.of(0,3));
            searchRecord = recordRepository.findByIdInOrderByCreatedAtDesc(recordIds,PageRequest.of(0,3));

            if(searchRecord.getTotalElements() == 0 && searchArticle.getTotalElements() == 0)
                throw new SearchException(Code.SEARCH_NO_DATA);
        }

        return SearchConverter.toSearchPreViewDto(searchArticle,searchRecord,user);
    }
}
