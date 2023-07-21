package fithub.app.converter;

import fithub.app.domain.Article;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.web.dto.responseDto.SearchPreViewResponseDto;

import java.util.List;

public class SearchConverter {

    public static SearchPreViewResponseDto.SearchPreViewDto toSearchPreViewDto(List<Article> articleList, List<Record> recordList, User user){
        return SearchPreViewResponseDto.SearchPreViewDto.builder()
                .articlePreview(ArticleConverter.toArticleDtoList(articleList,user))
                .recordPreview(RecordConverter.toRecordDtoList(recordList,user))
                .build();
    }
}
