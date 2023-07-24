package fithub.app.converter;

import fithub.app.aws.s3.AmazonS3Manager;
import fithub.app.base.Code;
import fithub.app.base.exception.handler.ArticleException;
import fithub.app.domain.*;
import fithub.app.domain.mapping.ArticleHashTag;
import fithub.app.repository.ArticleRepositories.ArticleRepository;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import fithub.app.web.dto.responseDto.ArticleResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleConverter {

    Logger logger = LoggerFactory.getLogger(ArticleConverter.class);

    private final ArticleRepository articleRepository;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private final AmazonS3Manager amazonS3Manager;
    private static ArticleRepository staticArticleRepository;
    private static ExerciseCategoryRepository staticExerciseCategoryRepository;
    private static AmazonS3Manager staticAmazonS3Manager;

    private static Logger staticLogger;

    private static String pattern = "https://cmc-fithub\\.s3\\.ap-northeast-2\\.amazonaws\\.com(.*)";


    @PostConstruct
    public void init() {
        staticArticleRepository = this.articleRepository;
        staticExerciseCategoryRepository = this.exerciseCategoryRepository;
        staticAmazonS3Manager = this.amazonS3Manager;
        staticLogger = this.logger;
    }

    public static Article toArticle(ArticleRequestDto.CreateArticleDto request, User user, List<HashTag> hashTagList, Integer categoryId)throws IOException
    {
        ExerciseCategory exerciseCategory= staticExerciseCategoryRepository.findById(categoryId).get();
        Article article = Article.builder()
                .title(request.getTitle())
                .contents(request.getTitle())
                .user(user)
                .articleHashTagList(new ArrayList<>())
                .articleImageList(new ArrayList<>())
                .exerciseCategory(exerciseCategory)
                .build();

        staticLogger.info("생성된 article : {}", article.toString());
        article.setArticleHashTagList(toArticleHashTagList(hashTagList, article));
        article.setUser(user);

        // 사진 업로드 하기
        List<MultipartFile> articleImageList= request.getPictureList();
        if(articleImageList != null && !articleImageList.isEmpty()){
            createAndMapArticleImage(articleImageList,article);
        }
        return article;
    }

    public static Article toUpdateArticle(Article article,ArticleRequestDto.UpdateArticleDto request, List<HashTag> hashTagList) throws IOException
    {
        ExerciseCategory exerciseCategory = staticExerciseCategoryRepository.findById(request.getCategory()).orElseThrow(() -> new ArticleException(Code.CATEGORY_ERROR));
        article.update(request, exerciseCategory);
        article.setArticleHashTagList(toArticleHashTagList(hashTagList, article));

        List<MultipartFile> articleImageList = request.getNewPictureList();
        if(articleImageList != null && !articleImageList.isEmpty()){
            createAndMapArticleImage(articleImageList,article);
        }
        return article;
    }

    public static void createAndMapArticleImage(List<MultipartFile> articleImageList, Article article) throws IOException
    {
        for(int i = 0; i < articleImageList.size(); i++){
            MultipartFile image = articleImageList.get(i);
            try{
                Uuid uuid = staticAmazonS3Manager.createUUID();
                String KeyName = staticAmazonS3Manager.generateArticleKeyName(uuid, image.getOriginalFilename());
                String fileUrl = staticAmazonS3Manager.uploadFile(KeyName, image);
                staticLogger.info("S3에 업로드 한 파일의 url : {}", fileUrl);
                ArticleImage articleImage = ArticleImageConverter.toArticleImage(fileUrl, article, uuid);
                articleImage.setArticle(article);
            }catch (IOException e) {
                staticLogger.error("파일 업로드 에러 발생");
                throw new RuntimeException("IOException occurred while upload image...", e);
            }
        }
//        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(articleImageList.size(), 5));
//        List<CompletableFuture<Void>> futures = articleImageList.stream()
//                .map((image) -> CompletableFuture.runAsync(() -> {
//
//                }, executorService))
//                .collect(Collectors.toList());
    }

    private static List<ArticleHashTag> toArticleHashTagList(List<HashTag> hashTagList, Article article){
        return hashTagList.stream()
                .map(hashTag -> {
                    ArticleHashTag articleHashTag = ArticleHashTag.builder().build();
                    articleHashTag.setArticle(article);
                    articleHashTag.setHashTag(hashTag);
                    return articleHashTag;
                }).collect(Collectors.toList());
    }

    public static ArticleResponseDto.ArticleSpecDto toArticleSpecDto(Article article,
                                                                     Boolean isLiked, Boolean isScraped){
        return ArticleResponseDto.ArticleSpecDto.builder()
                .articleId(article.getId())
                .articleCategory(ExerciseCategoryConverter.toCategoryDto(article.getExerciseCategory()))
                .userInfo(UserConverter.toCommunityUserInfo(article.getUser()))
                .title(article.getTitle())
                .contents(article.getContents())
                .comments(article.getComments())
                .articlePictureList(PictureConverter.toPictureDtoList(article.getArticleImageList()))
                .createdAt(article.getCreatedAt())
                .Hashtags(HashTagConverter.toHashtagDtoList(article.getArticleHashTagList()))
                .likes(article.getLikes())
                .scraps(article.getSaves())
                .isLiked(isLiked)
                .isScraped(isScraped)
                .build();
    }

    public static ArticleResponseDto.ArticleDto toArticleDto(Article article, User user){
        return ArticleResponseDto.ArticleDto.builder()
                .articleId(article.getId())
                .userInfo(UserConverter.toCommunityUserInfo(article.getUser()))
                .articleCategory(ExerciseCategoryConverter.toCategoryDto(article.getExerciseCategory()))
                .title(article.getTitle())
                .contents(article.getContents())
                .pictureUrl(article.getArticleImageList().size() == 0 ? null : article.getArticleImageList().get(0).getImageUrl())
                .exerciseTag(article.getExerciseCategory().getName())
                .likes(article.getLikes())
                .comments(article.getComments())
                .isLiked(user.isLikedArticle(article))
                .createdAt(article.getCreatedAt())
                .build();
    }

    public static ArticleResponseDto.ArticleDtoList toArticleDtoList(List<Article> articleList, User user){
        List<ArticleResponseDto.ArticleDto> articleDtoList =
                articleList.stream()
                        .map(article -> toArticleDto(article, user))
                        .collect(Collectors.toList());

        return ArticleResponseDto.ArticleDtoList.builder()
                .articleList(articleDtoList)
                .size(articleDtoList.size())
                .build();
    }

    public static ArticleResponseDto.ArticleCreateDto toArticleCreateDto(Article article){
        return ArticleResponseDto.ArticleCreateDto.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .ownerId(article.getUser().getId())
                .createdAt(article.getCreatedAt())
                .build();
    }

    public static ArticleResponseDto.ArticleUpdateDto toArticleUpdateDto(Article article){
        return ArticleResponseDto.ArticleUpdateDto.builder()
                .articleId(article.getId())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    public static ArticleResponseDto.ArticleDeleteDto toArticleDeleteDto(Long id){
        return ArticleResponseDto.ArticleDeleteDto.builder()
                .articleId(id)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public ArticleResponseDto.ArticleDeleteDtoList toArticleDeleteDtoList(List<Long> idList){

        List<ArticleResponseDto.ArticleDeleteDto> articleDeleteDtoList =
                idList.stream()
                        .map(id -> toArticleDeleteDto(id))
                        .collect(Collectors.toList());

        return ArticleResponseDto.ArticleDeleteDtoList.builder()
                .deletedArticleList(articleDeleteDtoList)
                .size(articleDeleteDtoList.size())
                .build();
    }

    public static ArticleResponseDto.ArticleLikeDto toArticleLikeDto(Article article){
        return ArticleResponseDto.ArticleLikeDto.builder()
                .articleId(article.getId())
                .articleLikes(article.getLikes())
                .build();
    }

    public static ArticleResponseDto.ArticleSaveDto toArticleSaveDtoDto(Article article){
        return ArticleResponseDto.ArticleSaveDto.builder()
                .articleId(article.getId())
                .articleSaves(article.getSaves())
                .build();
    }

    public static String toKeyName(String imageUrl) {
        String input = imageUrl;

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);
        String extractedString = null;
        if (matcher.find())
            extractedString = matcher.group(1);

        return extractedString;
    }
}
