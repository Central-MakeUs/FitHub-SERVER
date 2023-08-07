package fithub.app.service.impl;

import fithub.app.aws.s3.AmazonS3Manager;
import fithub.app.base.Code;
import fithub.app.base.exception.handler.ArticleException;
import fithub.app.converter.ArticleConverter;
import fithub.app.converter.HashTagConverter;
import fithub.app.domain.*;
import fithub.app.domain.enums.ContentsType;
import fithub.app.domain.enums.NotificationCategory;
import fithub.app.domain.mapping.ArticleHashTag;
import fithub.app.domain.mapping.ArticleLikes;
import fithub.app.domain.mapping.ContentsReport;
import fithub.app.domain.mapping.SavedArticle;
import fithub.app.firebase.service.FireBaseService;
import fithub.app.repository.ArticleRepositories.*;
import fithub.app.repository.ContentsReportRepository;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.repository.HashTagRepositories.ArticleHashTagRepository;
import fithub.app.repository.HashTagRepositories.HashTagRepository;
import fithub.app.repository.NotificationRepository;
import fithub.app.repository.UserRepository;
import fithub.app.service.ArticleService;
import fithub.app.utils.FCMType;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;
    private final HashTagRepository hashTagRepository;

    private final ArticleLikesRepository articleLikesRepository;

    private final SavedArticleRepository savedArticleRepository;

    private final ArticleImageRepository articleImageRepository;

    private final ArticleHashTagRepository articleHashTagRepository;

    private final AmazonS3Manager amazonS3Manager;

    private final ContentsReportRepository contentsReportRepository;

    private final UserRepository userRepository;
    @Value("${paging.size}")
    Integer size;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private final FireBaseService fireBaseService;

    private final NotificationRepository notificationRepository;

    String alarmTitle = "FITHUB";

    String alarmBodyHad = "님이 나의 [";

    String alarmBodyMiddle = "]핏 사이트 글에 좋아요를 눌렀어요";

    @Override
    @Transactional(readOnly = false)
    public Article create(ArticleRequestDto.CreateArticleDto request, User user, Integer categoryId) throws IOException
    {
        String exerciseTag = request.getExerciseTag();
        HashTag exercisehashTag = hashTagRepository.findByName(exerciseTag).orElseGet(() -> HashTagConverter.newHashTag(exerciseTag));
        List<HashTag> hashTagList = new ArrayList<>();
        if (request.getTagList() != null) {
         hashTagList =request.getTagList().stream()
                    .map(tag -> hashTagRepository.findByName(tag).orElseGet(() -> HashTagConverter.newHashTag(tag)))
                    .collect(Collectors.toList());
        }

        hashTagList.add(0,exercisehashTag);

        logger.info("해시 태그의 리스트 : {}", hashTagList.toString());
        Article article = ArticleConverter.toArticle(request, user, hashTagList, categoryId);

        logger.error("============================================================================================");
        logger.error("만들어 진 게시글의 결과 {}",article.toString());
        return articleRepository.save(article);
    }

    @Override
    public Article getArticle(Long ArticleId) {
        return articleRepository.findById(ArticleId).orElseThrow(()-> new ArticleException(Code.ARTICLE_NOT_FOUND));
    }

    @Override
    public Boolean getIsSaved(Article article, User user){
        Optional<SavedArticle> isSaved = savedArticleRepository.findByArticleAndUser(article, user);
        return isSaved.isPresent();
    }

    @Override
    public Boolean getIsLiked(Article article, User user){
        Optional<ArticleLikes> isLiked = articleLikesRepository.findByArticleAndUser(article, user);
        return isLiked.isPresent();
    }

    @Override
    @Transactional(readOnly = false)
    public Article toggleArticleLike(Long articleId, User user) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(Code.ARTICLE_NOT_FOUND));
        Optional<ArticleLikes> articleLike = articleLikesRepository.findByArticleAndUser(article, user);

        Article updatedArticle;

        if(articleLike.isPresent()){
            articleLike.get().getUser().getArticleLikesList().remove(articleLike.get());
            articleLikesRepository.delete(articleLike.get());
            updatedArticle = article.likeToggle(false);
        }else{
            updatedArticle = article.likeToggle(true);
            ArticleLikes articleLikes = ArticleLikes.builder()
                    .article(updatedArticle)
                    .user(user)
                    .build();
            articleLikes.setUser(user);
            articleLikesRepository.save(articleLikes);
        }

        return updatedArticle;
    }

    @Override
    @Transactional(readOnly = false)
    public Article toggleArticleSave(Long articleId, User user) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(Code.ARTICLE_NOT_FOUND));
        Optional<SavedArticle> savedArticle = savedArticleRepository.findByArticleAndUser(article, user);

        Article updatedArticle;
        if(savedArticle.isPresent()){
            savedArticle.get().getUser().getArticleLikesList().remove(savedArticle.get());
            savedArticleRepository.delete(savedArticle.get());
            updatedArticle = article.saveToggle(false);
        }else{
            updatedArticle = article.saveToggle(true);
            SavedArticle newSavedArticle = SavedArticle.builder()
                    .article(article)
                    .user(user)
                    .build();
            newSavedArticle.setUser(user);
            savedArticleRepository.save(newSavedArticle);
        }

        return updatedArticle;
    }

    @Override
    @Transactional(readOnly = false)
    public Article updateArticle(Long articleId, ArticleRequestDto.UpdateArticleDto request, User user) throws IOException
    {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(Code.ARTICLE_NOT_FOUND));

        if(!article.getUser().getId().equals(user.getId()))
            throw new ArticleException(Code.ARTICLE_FORBIDDEN);

        List<String> images = article.getArticleImageList().stream()
                .map(articleImage -> articleImage.getImageUrl())
                .collect(Collectors.toList());

        List<String> deleteTarget = images.stream()
                .filter(image -> !request.getRemainPictureUrlList().contains(image))
                .collect(Collectors.toList());

        for(int i = 0; i < deleteTarget.size(); i++)
            logger.info("삭제할 사진 : {}", deleteTarget.get(i));

        for(int i = 0; i < deleteTarget.size(); i++) {
            String s = deleteTarget.get(i);
            ArticleImage articleImage = articleImageRepository.findByImageUrl(s).get();
            article.getArticleImageList().remove(articleImage);
            articleImageRepository.delete(articleImage);
            String Keyname = ArticleConverter.toKeyName(deleteTarget.get(i));
            amazonS3Manager.deleteFile(Keyname.substring(1));
        }

        // 필요 없어진 사진은 지웠으니 이제 게시글과 연결된 해시태그 재 설정
        List<ArticleHashTag> articleHashTagList = article.getArticleHashTagList();

        for(int i = 0; i < articleHashTagList.size(); i++) {
            ArticleHashTag articleHashTag = articleHashTagList.get(i);
            article.getArticleHashTagList().remove(articleHashTag);
            articleHashTagRepository.delete(articleHashTag);
        }

        if (articleHashTagList.size() > 0) {
            ArticleHashTag last = articleHashTagList.get(0);
            articleHashTagList.remove(last);
            articleHashTagRepository.delete(last);
        }

        String exerciseTag =  request.getExerciseTag();
        HashTag exercisehashTag = hashTagRepository.findByName(exerciseTag).orElseGet(() -> HashTagConverter.newHashTag(exerciseTag));

        List<HashTag> hashTagList = new ArrayList<>();
        if (request.getHashTagList() != null) {
            hashTagList = request.getHashTagList().stream()
                    .map(tag -> hashTagRepository.findByName(tag).orElseGet(() -> HashTagConverter.newHashTag(tag)))
                    .collect(Collectors.toList());
        }

        hashTagList.add(0,exercisehashTag);

        return ArticleConverter.toUpdateArticle(article,request,hashTagList);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteArticleSingle(Long articleId, User user) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(Code.ARTICLE_NOT_FOUND));

        if(!article.getUser().getId().equals(user.getId()))
            throw new ArticleException(Code.ARTICLE_FORBIDDEN);

        List<ArticleImage> articleImageList = article.getArticleImageList();

        for(int i = 0; i < articleImageList.size(); i++) {
            String s = articleImageList.get(i).getImageUrl();
            String Keyname = ArticleConverter.toKeyName(s);
            amazonS3Manager.deleteFile(Keyname.substring(1));
        }

        articleRepository.delete(article);
    }

    @Override
    public Page<Article> findArticlePagingCategoryAndCreatedAt(User user, Integer categoryId, Integer pageIndex) {

        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new ArticleException(Code.CATEGORY_ERROR));

        Page<Article> findArticle = null;

        if(pageIndex == null)
            pageIndex = 0;

        findArticle = articleRepository.findAllByExerciseCategoryOrderByCreatedAtDesc(exerciseCategory,user,PageRequest.of(pageIndex, size));
        return findArticle;
    }

    @Override
    public Page<Article> findArticlePagingCreatedAt(User user, Integer pageIndex) {
        Page<Article> findArticle = null;

        if(pageIndex == null)
            pageIndex = 0;
        findArticle = articleRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(pageIndex, size), user);
        return findArticle;
    }

    @Override
    public Page<Article> findArticlePagingCategoryAndLikes(User user, Integer categoryId, Integer pageIndex) {

        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new ArticleException(Code.CATEGORY_ERROR));

        Page<Article> findArticle = null;

        if(pageIndex == null)
            pageIndex = 0;

        findArticle = articleRepository.findAllByExerciseCategoryOrderByLikesDescCreatedAtDesc(exerciseCategory,user,PageRequest.of(pageIndex, size));
        return findArticle;
    }

    @Override
    public Page<Article> findArticlePagingLikes(User user, Integer pageIndex) {
        Page<Article> findArticle = null;

        if(pageIndex == null)
            pageIndex = 0;
        findArticle = articleRepository.findAllByOrderByLikesDescCreatedAtDesc(PageRequest.of(pageIndex, size), user);
        return findArticle;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteArticleBulk(ArticleRequestDto.DeleteListArticleDto request, User user) {
        List<Article> articleList = articleRepository.findByIdIn(request.getArticleIdList());
        for(Article article : articleList){
            if (!article.getUser().getId().equals(user.getId()))
                throw new ArticleException(Code.ARTICLE_FORBIDDEN);

            List<ArticleImage> articleImageList = article.getArticleImageList();
            for (ArticleImage articleImage : articleImageList) {
                String s = articleImage.getImageUrl();
                String Keyname = ArticleConverter.toKeyName(s);
                amazonS3Manager.deleteFile(Keyname.substring(1));
            }
            articleRepository.delete(article);
        }
    }

    @Override
    @Transactional
    public ContentsReport reportArticle(Long articleId, User user) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(Code.ARTICLE_NOT_FOUND));
        if(article.getUser().getId().equals(user.getId()))
            throw new ArticleException(Code.MY_CONTENTS);
        Optional<ContentsReport> findReport = contentsReportRepository.findByUserAndArticle(user, article);
        if (findReport.isPresent())
            throw new ArticleException(Code.ALREADY_REPORT);
        User articleOwner = userRepository.findById(article.getUser().getId()).get();
        articleOwner.countReport();
        article.countReport();

         return contentsReportRepository.save(ContentsReport.builder()
                .contentsType(ContentsType.ARTICLE)
                .article(article)
                .user(user)
                .build());
    }

    @Override
    @Transactional
    public void alarmArticleLike(Article article, User user) throws IOException
    {

        // 알림 보내기
        for(FcmToken fcmToken : article.getUser().getFcmTokenList()){
            fireBaseService.sendMessageTo(fcmToken.getToken(),alarmTitle,user.getNickname().toString() + alarmBodyHad + article.getTitle() + alarmBodyMiddle,FCMType.ARTICLE.toString(),article.getId().toString());
        }
        // 알림 테이블에 저장
        Notification notification = notificationRepository.save(Notification.builder()
                .notificationCategory(NotificationCategory.ARTICLE)
                .article(article)
                .user(article.getUser())
                .notificationBody(user.getNickname().toString() + alarmBodyHad + article.getTitle() + alarmBodyMiddle)
                .build());

        notification.setUser(article.getUser());
    }
}
