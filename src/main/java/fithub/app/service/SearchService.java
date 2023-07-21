package fithub.app.service;

import fithub.app.domain.Article;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.web.dto.responseDto.SearchPreViewResponseDto;
import org.springframework.data.domain.Page;

public interface SearchService {

    Page<Article> searchArticleCreatedAt(String tag, Long last);
    Page<Record> searchRecordCreatedAt(String tag, Long last);
    Page<Article> searchArticleLikes(String tag, Long last);
    Page<Record> searchRecordLikes(String tag, Long last);

    SearchPreViewResponseDto.SearchPreViewDto searchPreview(String tag, User user);
}
