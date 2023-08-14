package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.Code;
import fithub.app.base.ResponseDto;
import fithub.app.converter.ArticleConverter;
import fithub.app.converter.RecordConverter;
import fithub.app.domain.Article;
import fithub.app.domain.RecommendArticleKeyword;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "검색 API", description = "해시태그 검색 API 모음")
public class SearchRestController {

    Logger logger = LoggerFactory.getLogger(SearchRestController.class);
    private final SearchService searchService;

    @Operation(summary = "검색 API - 전체 미리보기 ✔️🔑", description = "tag에 검색 태그를 담아서 전달")
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
        logger.info("검색 태그 : {}",tag);
        SearchPreViewResponseDto.SearchPreViewDto searchPreViewDto = searchService.searchPreview(tag, user);
        return ResponseDto.of(searchPreViewDto);
    }

    @Operation(summary = "게시글 검색 API - 최신순 ✔️🔑", description = "categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 검색결과 있음"),
            @ApiResponse(responseCode = "2021", description = "OK : 검색결과 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "tag", description = "검색하려는 태그"),
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠"),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search/articles")
    public ResponseDto<ArticleResponseDto.ArticleDtoList> articleSearchCreatedAt(@RequestParam(name = "tag") String tag,@RequestParam(name = "pageIndex") Integer pageIndex, @AuthUser User user){
        logger.info("검색 태그 : {}",tag);
        logger.info("last의 값 : {}",pageIndex);
        Page<Article> articles = searchService.searchArticleCreatedAt(tag, pageIndex,user);
        logger.info("검색 결과의 갯수 : {}", articles.toList().size());
        if(articles == null || articles.getTotalElements() == 0)
            return ResponseDto.of(Code.SEARCH_NO_DATA, null);
        else
            return ResponseDto.of(ArticleConverter.toArticleDtoList(articles, user));
    }

    @Operation(summary = "게시글 검색 API - 인기순 ✔️🔑", description = "categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 검색결과 있음"),
            @ApiResponse(responseCode = "2021", description = "OK : 검색결과 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "tag", description = "검색하려는 태그"),
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠"),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search/articles/likes")
    public ResponseDto<ArticleResponseDto.ArticleDtoList> articleSearchLikes(@RequestParam(name = "tag") String tag,@RequestParam(name = "pageIndex", required = false) Integer pageIndex, @AuthUser User user){
        logger.info("검색 태그 : {}",tag);
        logger.info("last의 값 : {}",pageIndex);
        Page<Article> articles = searchService.searchArticleLikes(tag, pageIndex,user);
        logger.info("검색 결과의 갯수 : {}", articles.toList().size());
        if(articles == null || articles.getTotalElements() == 0)
            return ResponseDto.of(Code.SEARCH_NO_DATA, null);
        else
            return ResponseDto.of(ArticleConverter.toArticleDtoList(articles, user));
    }

    @Operation(summary = "인증 검색 API - 최신순 ✔️🔑", description = "categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "2021", description = "OK : 검색결과 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "tag", description = "검색하려는 태그"),
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠"),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search/records")
    public ResponseDto<RecordResponseDto.recordDtoList> recordSearchCreatedAt(@RequestParam(name = "tag") String tag,@RequestParam(name = "pageIndex") Integer pageIndex, @AuthUser User user){
        logger.info("검색 태그 : {}",tag);
        logger.info("last의 값 : {}",pageIndex);
        Page<Record> records = searchService.searchRecordCreatedAt(tag, pageIndex,user);
        logger.info("검색 결과의 갯수 : {}", records.toList().size());
        if(records == null || records.getTotalElements() == 0)
            return ResponseDto.of(Code.SEARCH_NO_DATA, null);
        else
            return ResponseDto.of(RecordConverter.toRecordDtoList(records, user));
    }

    @Operation(summary = "인증 검색 API - 인기순 ✔️🔑", description = "categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "2021", description = "OK : 검색결과 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "tag", description = "검색하려는 태그"),
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠"),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search/records/likes")
    public ResponseDto<RecordResponseDto.recordDtoList> recordSearchLikes(@RequestParam(name = "tag") String tag,@RequestParam(name = "pageIndex", required = false) Integer pageIndex, @AuthUser User user){
        logger.info("검색 태그 : {}",tag);
        logger.info("last의 값 : {}",pageIndex);
        Page<Record> records = searchService.searchRecordLikes(tag, pageIndex,user);
        logger.info("검색 결과의 갯수 : {}", records.toList().size());
        if(records == null || records.getTotalElements() == 0)
            return ResponseDto.of(Code.SEARCH_NO_DATA, null);
        else
            return ResponseDto.of(RecordConverter.toRecordDtoList(records, user));
    }


    @Operation(summary = "게시글 검색 추천 해시태그 조회 API 🔑✔️", description = "게시글 검색 추천 해시태그 조회 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/search/articles/recommend-keyword")
    public ResponseDto<ArticleResponseDto.ArticleRecommendKeywordDto> getRecommendKeyword(){
        List<RecommendArticleKeyword> recommendArticleKeywordList = searchService.getRecommendArticleKeyword();
        return ResponseDto.of(ArticleConverter.toArticleRecommendKeywordDto(recommendArticleKeywordList));
    }
}
