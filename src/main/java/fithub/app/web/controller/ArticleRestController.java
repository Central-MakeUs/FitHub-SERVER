package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.ResponseDto;
import fithub.app.converter.ArticleConverter;
import fithub.app.domain.Article;
import fithub.app.domain.User;
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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
public class ArticleRestController {

    Logger logger = LoggerFactory.getLogger(ArticleRestController.class);

    private final ArticleService articleService;

    @Operation(summary = "게시글 상세조회 API", description = "게시글의 id를 통해 상세조회하는 API 입니다. 댓글 정보는 api를 하나 더 호출해주세요!")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 응답이 복잡하니 주의!"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true)
    })
    @GetMapping("/articles/{articleId}/spec")
    public ResponseDto<ArticleResponseDto.ArticleSpecDto> articleSpec(@PathVariable(name = "articleId") @ExistArticle Long articleId, @AuthUser User user){

        Article article = articleService.getArticle(articleId);
        Boolean isLiked = articleService.getIsLiked(article, user);
        Boolean isSaved = articleService.getIsSaved(article, user);
        return ResponseDto.of(ArticleConverter.toArticleSpecDto(article,isLiked,isSaved));
    }

    @GetMapping("/articles")
    public ResponseEntity<ArticleResponseDto.ArticleDtoList> articleTimeList(@RequestParam Long last){
        return null;
    }

    @GetMapping("/articles/likes")
    public ResponseEntity<ArticleResponseDto.ArticleDtoList> articleLikesList(@RequestParam Long last){
        return null;
    }

    @GetMapping("/articles/{categoryId}")
    public ResponseEntity<ArticleResponseDto.ArticleDtoList> articleCategoryTimeList(@PathVariable Long categoryId){
        return  null;
    }

    @GetMapping("/articles/{category}/likes")
    public ResponseEntity<ArticleResponseDto.ArticleDtoList> articleCategoryLikesList(@PathVariable Long category){
        return null;
    }

    @Operation(summary = "게시글 추가 API", description = "게시글 추가 API 입니다. 사진 여러 장을 한번에 보내 주세요")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true)
    })
    @PostMapping(value = "/articles/{categoryId}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<ArticleResponseDto.ArticleCreateDto> createArticle(@ModelAttribute ArticleRequestDto.CreateArticleDto request, @PathVariable @ExistCategory Integer categoryId, @AuthUser User user) throws IOException {

        logger.info("사용자가 건네준 정보 : {}", request.toString());
        Article article = articleService.create(request, user, categoryId);
        return ResponseDto.of(ArticleConverter.toArticleCreateDto(article));
    }

    @PatchMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponseDto.ArticleUpdateDto> updateArticle(@PathVariable Long id ,@RequestBody ArticleRequestDto.UpdateArticleDto request, @AuthUser User user){
        return null;
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponseDto.ArticleDeleteDto> deleteArticle(@PathVariable Long id, @AuthUser User user){
        return null;
    }

    @DeleteMapping("/articles")
    public ResponseEntity<ArticleResponseDto.ArticleDeleteDtoList> deleteListArticle(@RequestBody ArticleRequestDto.DeleteListArticleDto request, @AuthUser User user){
        return null;
    }

    @Operation(summary = "게시글 좋아요 누르기/취소",description = "좋아요를 누른 적이 있다면 취소, 없다면 좋아요 누르기 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 성공 시 새로 바뀐 좋아요 갯수 응답에 포함"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true)
    })
    @PostMapping("/articles/{articleId}/likes")
    public ResponseDto<ArticleResponseDto.ArticleLikeDto> likeArticle(@PathVariable(name = "articleId") @ExistArticle Long articleId, @AuthUser User user){
        Article article = articleService.toggleArticleLike(articleId, user);
        return ResponseDto.of(ArticleConverter.toArticleLikeDto(article));
    }

    @Operation(summary = "게시글 저장/취소",description = "저장을 한 적이 있다면 취소, 없다면 저장하기 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 성공 시 새로 바뀐 저장 갯수 응답에 포함"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true)
    })
    @PostMapping("/articles/{articleId}/scrap")
    public ResponseDto<ArticleResponseDto.ArticleSaveDto> scrapArticle(@PathVariable("articleId") @ExistArticle Long articleId, @AuthUser User user){
        Article article = articleService.toggleArticleSave(articleId, user);
        return ResponseDto.of(ArticleConverter.toArticleSaveDtoDto(article));
    }
}
