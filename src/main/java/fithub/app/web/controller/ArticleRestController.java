package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.ResponseDto;
import fithub.app.converter.ArticleConverter;
import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.service.ArticleService;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import fithub.app.web.dto.responseDto.ArticleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Validated
@RequiredArgsConstructor
public class ArticleRestController {

    Logger logger = LoggerFactory.getLogger(ArticleRestController.class);

    private final ArticleService articleService;

    @GetMapping("/article/{id}")
    public ResponseEntity<ArticleResponseDto.ArticleDto> articleSpec(@PathVariable Long id){
        return null;
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
    @Parameters({
            @Parameter(name = "user", hidden = true)
    })
    @PostMapping(value = "/articles/{categoryId}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<ArticleResponseDto.ArticleCreateDto> createArticle(@ModelAttribute @Valid ArticleRequestDto.CreateArticleDto request,@PathVariable Integer categoryId, @AuthUser User user) throws IOException {

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

    @PostMapping("/articles/{articleId}/likes")
    public ResponseEntity<ArticleResponseDto.ArticleLikeDto> likeArticle(@PathVariable Long id, @AuthUser User user){
        return null;
    }

    @PostMapping("/articles/{articleId}/scrap")
    public ResponseEntity<ArticleResponseDto.ArticleScrapDto> scrapArticle(@PathVariable Long id, @AuthUser User user){
        return null;
    }
}
