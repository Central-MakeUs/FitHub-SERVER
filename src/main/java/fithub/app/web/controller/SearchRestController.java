package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.Code;
import fithub.app.base.ResponseDto;
import fithub.app.converter.ArticleConverter;
import fithub.app.converter.RecordConverter;
import fithub.app.domain.Article;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.service.SearchService;
import fithub.app.web.dto.responseDto.ArticleResponseDto;
import fithub.app.web.dto.responseDto.RecordResponseDto;
import fithub.app.web.dto.responseDto.SearchPreViewResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "검색 API", description = "해시태그 검색 API 모음")
public class SearchRestController {


    private final SearchService searchService;

    @Operation(summary = "게시글 검색 API - 전체 미리보기 ✔️", description = "tag에 검색 태그를 담아서 전달")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 검색결과 있음"),
            @ApiResponse(responseCode = "2021", description = "OK : 검색결과 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "tag", description = "검색하려는 태그"),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search")
    public ResponseDto<SearchPreViewResponseDto.SearchPreViewDto> articleSearchPreView(@RequestParam(name = "tag") String tag, @AuthUser User user){
        SearchPreViewResponseDto.SearchPreViewDto searchPreViewDto = searchService.searchPreview(tag, user);
        return ResponseDto.of(searchPreViewDto);
    }

    @Operation(summary = "게시글 검색 API - 최신순 ✔️", description = "tag에 검색 태그를 담아서 전달, last로 페이징")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 검색결과 있음"),
            @ApiResponse(responseCode = "2021", description = "OK : 검색결과 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "tag", description = "검색하려는 태그"),
            @Parameter(name = "last", description = "스크롤의 마지막에 존재하는 인증의 Id, 이게 있으면 다음 스크롤", required = false),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search/articles")
    public ResponseDto<ArticleResponseDto.ArticleDtoList> articleSearchCreatedAt(@RequestParam(name = "tag") String tag,@RequestParam(name = "last", required = false) Long last, @AuthUser User user){
        Page<Article> articles = searchService.searchArticleCreatedAt(tag, last);
        if(articles == null || articles.getTotalElements() == 0)
            return ResponseDto.of(Code.SEARCH_NO_DATA, null);
        else
            return ResponseDto.of(ArticleConverter.toArticleDtoList(articles.toList(), user));
    }

    @Operation(summary = "게시글 검색 API - 인기순 ✔️", description = "tag에 검색 태그를 담아서 전달, last로 페이징")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 검색결과 있음"),
            @ApiResponse(responseCode = "2021", description = "OK : 검색결과 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "tag", description = "검색하려는 태그"),
            @Parameter(name = "last", description = "스크롤의 마지막에 존재하는 인증의 Id, 이게 있으면 다음 스크롤", required = false),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search/records")
    public ResponseDto<ArticleResponseDto.ArticleDtoList> articleSearchLikes(@RequestParam(name = "tag") String tag,@RequestParam(name = "last", required = false) Long last, @AuthUser User user){
        Page<Article> articles = searchService.searchArticleLikes(tag, last);
        if(articles == null || articles.getTotalElements() == 0)
            return ResponseDto.of(Code.SEARCH_NO_DATA, null);
        else
            return ResponseDto.of(ArticleConverter.toArticleDtoList(articles.toList(), user));
    }

    @Operation(summary = "인증 검색 API - 최신순 ✔️", description = "tag에 검색 태그를 담아서 전달, last로 페이징")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "2021", description = "OK : 검색결과 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "tag", description = "검색하려는 태그"),
            @Parameter(name = "last", description = "스크롤의 마지막에 존재하는 인증의 Id, 이게 있으면 다음 스크롤", required = false),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search/articles/likes")
    public ResponseDto<RecordResponseDto.recordDtoList> recordSearchCreatedAt(@RequestParam(name = "tag") String tag,@RequestParam(name = "last", required = false) Long last, @AuthUser User user){
        Page<Record> records = searchService.searchRecordCreatedAt(tag, last);
        if(records == null || records.getTotalElements() == 0)
            return ResponseDto.of(Code.SEARCH_NO_DATA, null);
        else
            return ResponseDto.of(RecordConverter.toRecordDtoList(records.toList(), user));
    }

    @Operation(summary = "인증 검색 API - 인기순 ✔️", description = "tag에 검색 태그를 담아서 전달, last로 페이징")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "2021", description = "OK : 검색결과 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "tag", description = "검색하려는 태그"),
            @Parameter(name = "last", description = "스크롤의 마지막에 존재하는 인증의 Id, 이게 있으면 다음 스크롤", required = false),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search/records/likes")
    public ResponseDto<RecordResponseDto.recordDtoList> recordSearchLikes(@RequestParam(name = "tag") String tag,@RequestParam(name = "last", required = false) Long last, @AuthUser User user){
        Page<Record> records = searchService.searchRecordLikes(tag, last);
        if(records == null || records.getTotalElements() == 0)
            return ResponseDto.of(Code.SEARCH_NO_DATA, null);
        else
            return ResponseDto.of(RecordConverter.toRecordDtoList(records.toList(), user));
    }
}
