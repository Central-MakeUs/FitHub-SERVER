package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.ResponseDto;
import fithub.app.service.converter.ArticleConverter;
import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ContentsReport;
import fithub.app.firebase.service.FireBaseService;
import fithub.app.service.ArticleService;
import fithub.app.validation.annotation.ExistArticle;
import fithub.app.validation.annotation.ExistCategory;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import fithub.app.web.dto.responseDto.ArticleResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 관련 API")
public class ArticleRestController {

    Logger logger = LoggerFactory.getLogger(ArticleRestController.class);

    private final ArticleService articleService;

    private final FireBaseService fireBaseService;

    @Operation(summary = "게시글 상세조회 API ✔️🔑 ", description = "게시글의 id를 통해 상세조회하는 API 입니다. 댓글 정보는 api를 하나 더 호출해주세요!")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 응답이 복잡하니 주의!"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "articleId", description = "게시글의 아이디"),
    })
    @GetMapping("/articles/{articleId}/spec")
    public ResponseDto<ArticleResponseDto.ArticleSpecDto> articleSpec(@PathVariable(name = "articleId") @ExistArticle Long articleId, @AuthUser User user){

        Article article = articleService.getArticle(articleId);
        return ResponseDto.of(ArticleConverter.toArticleSpecDto(article,user, article.getExerciseCategory()));
    }

    @Operation(summary = "게시글 목록 조회 API - 최신순 ✔️🔑", description = "categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 카테고리가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠"),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/articles/{categoryId}")
    public ResponseDto<ArticleResponseDto.ArticleDtoList> articleTimeList(@RequestParam(name = "pageIndex", required = false) Integer pageIndex,@PathVariable(name = "categoryId") @ExistCategory Integer categoryId, @AuthUser User user){
        Page<Article> articles = null;
        if (categoryId != 0)
            articles = articleService.findArticlePagingCategoryAndCreatedAt(user, categoryId, pageIndex);
        else
            articles = articleService.findArticlePagingCreatedAt(user,pageIndex);
        return ResponseDto.of(ArticleConverter.toArticleDtoList(articles, user, categoryId.equals(0)));
    }

    @Operation(summary = "게시글 목록 조회 API - 인기순 ✔️🔑", description = "categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 카테고리가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠"),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/articles/{categoryId}/likes")
    public ResponseDto<ArticleResponseDto.ArticleDtoList> articleLikesList(@RequestParam(name = "pageIndex") Integer pageIndex, @PathVariable(name = "categoryId") @ExistCategory Integer categoryId, @AuthUser User user){
        Page<Article> articles = null;
        if (categoryId != 0)
            articles = articleService.findArticlePagingCategoryAndLikes(user,categoryId,pageIndex);
        else
            articles = articleService.findArticlePagingLikes(user,pageIndex);

        return ResponseDto.of(ArticleConverter.toArticleDtoList(articles, user,categoryId.equals(0)));
    }

    @Operation(summary = "게시글 추가 API ✔️🔑", description = "게시글 추가 API 입니다. 사진 여러 장을 한번에 보내 주세요")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "categoryId", description = "카테고리 아이디")
    })
    @PostMapping(value = "/articles/{categoryId}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<ArticleResponseDto.ArticleCreateDto> createArticle(@ModelAttribute ArticleRequestDto.CreateArticleDto request, @PathVariable @ExistCategory Integer categoryId, @AuthUser User user) throws IOException {

        logger.info("사용자가 건네준 정보 : {}", request.toString());
        Article article = articleService.create(request, user, categoryId);
        return ResponseDto.of(ArticleConverter.toArticleCreateDto(article));
    }

    @Operation(summary = "게시글 수정 API ✔️🔑", description = "게시글 수정 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4032", description = "FORBIDDEN : 다른 사람의 게시글", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "articleId", description = "게시글 아이디")
    })
    @PatchMapping(value = "/articles/{articleId}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<ArticleResponseDto.ArticleUpdateDto> updateArticle(@PathVariable(name = "articleId") Long articleId ,@ModelAttribute ArticleRequestDto.UpdateArticleDto request, @AuthUser User user)throws IOException
    {

        logger.info("사용자가 건네준 정보 : {}", request.toString());
        Article updatedArticle = articleService.updateArticle(articleId, request, user);
        return ResponseDto.of(ArticleConverter.toArticleUpdateDto(updatedArticle));
    }

    @Operation(summary = "게시글 삭제 API ✔️🔑", description = "게시글 삭제 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4032", description = "FORBIDDEN : 다른 사람의 게시글", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "articleId", description = "게시글 아이디")
    })
    @DeleteMapping("/articles/{articleId}")
    public ResponseDto<ArticleResponseDto.ArticleDeleteDto> deleteArticle(@PathVariable(name = "articleId") Long articleId, @AuthUser User user){
        articleService.deleteArticleSingle(articleId, user);
        return ResponseDto.of(ArticleConverter.toArticleDeleteDto(articleId));
    }

    @Operation(summary = "게시글 여러개 삭제 API ✔️🔑- 마이 페이지에서 사용됨", description = "게시글 여러개 삭제 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4032", description = "FORBIDDEN : 다른 사람의 게시글 하나로 있음", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PatchMapping ("/articles")
    public ResponseDto<ArticleResponseDto.ArticleDeleteDtoList> deleteListArticle(@RequestBody ArticleRequestDto.DeleteListArticleDto request, @AuthUser User user){
        articleService.deleteArticleBulk(request, user);
        return ResponseDto.of(ArticleConverter.toArticleDeleteDtoList(request.getArticleIdList()));
    }

    @Operation(summary = "게시글 좋아요 누르기/취소 ✔️🔑",description = "좋아요를 누른 적이 있다면 취소, 없다면 좋아요 누르기 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 성공 시 새로 바뀐 좋아요 갯수 응답에 포함"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "articleId", description = "게시글 아이디")
    })
    @PostMapping("/articles/{articleId}/likes")
    public ResponseDto<ArticleResponseDto.ArticleLikeDto> likeArticle(@PathVariable(name = "articleId") @ExistArticle Long articleId, @AuthUser User user) throws IOException
    {
        Article article = articleService.toggleArticleLike(articleId, user);
        // 알림 보내기
        System.out.println(article.getUser().getCommunityPermit());
        if(user.isLikedArticle(article) && article.getUser().getCommunityPermit() && !article.getUser().getId().equals(user.getId()))
            articleService.alarmArticleLike(article,user);
        return ResponseDto.of(ArticleConverter.toArticleLikeDto(article,user));
    }

    @Operation(summary = "게시글 좋아요 누르기/취소 - apple ✔️🔑",description = "좋아요를 누른 적이 있다면 취소, 없다면 좋아요 누르기 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 성공 시 새로 바뀐 좋아요 갯수 응답에 포함"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "articleId", description = "게시글 아이디")
    })
    @PostMapping("/articles/{articleId}/likes/apple")
    public ResponseDto<ArticleResponseDto.ArticleLikeDto> likeArticleApple(@PathVariable(name = "articleId") @ExistArticle Long articleId, @AuthUser User user) throws IOException
    {
        Article article = articleService.toggleArticleLike(articleId, user);
        // 알림 보내기
        System.out.println(article.getUser().getCommunityPermit());
        if(user.isLikedArticle(article) && article.getUser().getCommunityPermit() && !article.getUser().getId().equals(user.getId()))
            articleService.alarmArticleLikeApple(article,user);
        return ResponseDto.of(ArticleConverter.toArticleLikeDto(article,user));
    }

    @Operation(summary = "게시글 저장/취소 ✔️🔑",description = "저장을 한 적이 있다면 취소, 없다면 저장하기 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 성공 시 새로 바뀐 저장 갯수 응답에 포함"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "articleId", description = "게시글 아이디")
    })
    @PostMapping("/articles/{articleId}/scrap")
    public ResponseDto<ArticleResponseDto.ArticleSaveDto> scrapArticle(@PathVariable("articleId") @ExistArticle Long articleId, @AuthUser User user){
        Article article = articleService.toggleArticleSave(articleId, user);
        return ResponseDto.of(ArticleConverter.toArticleSaveDtoDto(article, user));
    }

    @Operation(summary = "게시글 신고하기 ✔️🔑",description = "게시글을 신고하는 API이며 이미 신고한 경우는 안된다고 응답이 갑니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4061", description = "BAD_REQUEST : 이미 신고 했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4062", description = "BAD_REQUEST : 자신의 콘텐츠는 신고가 안됩니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "articleId", description = "게시글 아이디")
    })
    @PostMapping("/articles/{articleId}/report")
    public ResponseDto<ArticleResponseDto.ArticleReportDto> reportArticle(@PathVariable(name = "articleId") Long articleId, @AuthUser User user){
        ContentsReport reportArticle = articleService.reportArticle(articleId, user);
        return ResponseDto.of(ArticleConverter.toArticleReportDto(reportArticle, articleId));
    }
}
