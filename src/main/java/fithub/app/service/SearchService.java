package fithub.app.service;

import fithub.app.domain.Article;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.web.dto.responseDto.SearchPreViewResponseDto;
import org.springframework.data.domain.Page;

public interface SearchService {

    Page<Article> searchArticleCreatedAt(String tag, Integer pageIndex, User user);
    Page<Record> searchRecordCreatedAt(String tag, Integer pageIndex, User user);
    Page<Article> searchArticleLikes(String tag, Integer pageIndex, User user);
    Page<Record> searchRecordLikes(String tag, Integer pageIndex, User user);

    SearchPreViewResponseDto.SearchPreViewDto searchPreview(String tag, User user);
}
