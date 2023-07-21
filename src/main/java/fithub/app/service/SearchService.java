package fithub.app.service;

import fithub.app.domain.Article;
import fithub.app.domain.Record;
import org.springframework.data.domain.Page;

public interface SearchService {

    Page<Article> searchArticleCreatedAt(String tag, Long last);
    Page<Record> searchRecordCreatedAt(String tag, Long last);
    Page<Article> searchArticleLikes(String tag, Long last);
    Page<Article> searchRecordLikes(String tag, Long last);
}
