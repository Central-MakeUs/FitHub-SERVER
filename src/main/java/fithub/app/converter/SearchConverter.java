package fithub.app.converter;

import fithub.app.domain.Article;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.web.dto.responseDto.SearchPreViewResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class SearchConverter {

    public static SearchPreViewResponseDto.SearchPreViewDto toSearchPreViewDto(Page<Article> articleList, Page<Record> recordList, User user){
        return SearchPreViewResponseDto.SearchPreViewDto.builder()
                .articlePreview(ArticleConverter.toArticleDtoList(articleList,user,true))
                .recordPreview(RecordConverter.toRecordDtoList(recordList,user))
                .build();
    }
}
