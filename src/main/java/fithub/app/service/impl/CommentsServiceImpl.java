package fithub.app.service.impl;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.ArticleException;
import fithub.app.base.exception.handler.CommentsException;
import fithub.app.service.converter.CommentsConverter;
import fithub.app.domain.*;
import fithub.app.domain.enums.ContentsType;
import fithub.app.domain.enums.NotificationCategory;
import fithub.app.domain.mapping.CommentsLikes;
import fithub.app.domain.mapping.ContentsReport;
import fithub.app.firebase.service.FireBaseService;
import fithub.app.repository.ArticleRepositories.ArticleRepository;
import fithub.app.repository.CommentsRepository.CommentsLikesRepository;
import fithub.app.repository.CommentsRepository.CommentsRepository;
import fithub.app.repository.ContentsReportRepository;
import fithub.app.repository.NotificationRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.repository.UserRepository;
import fithub.app.service.CommentsService;
import fithub.app.utils.FCMType;
import fithub.app.web.dto.requestDto.CommentsRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final ArticleRepository articleRepository;
    private final RecordRepository recordRepository;

    private final CommentsLikesRepository commentsLikesRepository;

    Logger logger = LoggerFactory.getLogger(CommentsServiceImpl.class);

    private final ContentsReportRepository contentsReportRepository;

    private final UserRepository userRepository;

    private final FireBaseService fireBaseService;

    private final NotificationRepository notificationRepository;

    @Value("${paging.comments.size}")
    Integer size;

    String alarmTitle = "FITHUB";

    String alarmBodyHad = "님이 나의 [";

    String alarmBodyMiddle = "]핏 사이트 글에 [";

    String alarmBodyFoot = "] 댓글을 남겼어요";

    String alarmRecordBodyHead = "님이 나의 인증 글에 [";
    String alarmRecordBodyMiddle = "] 댓글을 남겼어요";

    @Override
    public Page<Comments> findOnArticle(Long id, Integer pageIndex, User user) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));

        Page<Comments> comments = null;

        if(pageIndex == null)
            pageIndex = 0;
        comments = commentsRepository.findByArticleOrderByCreatedAtDesc(article, user,user,PageRequest.of(pageIndex, size));
        return comments;
    }

    @Override
    public Page<Comments> findOnRecord(Long id, Integer pageIndex, User user) {
        Record record = recordRepository.findById(id).orElseThrow(() -> new CommentsException(Code.RECORD_NOT_FOUND));

        Page<Comments> comments = null;

        if(pageIndex == null)
            pageIndex = 0;
        comments = commentsRepository.findByRecordOrderByCreatedAtDesc(record, user,user,PageRequest.of(pageIndex, size));
        return comments;
    }

    @Override
    @Transactional(readOnly = false)
    public Comments createOnArticle(CommentsRequestDto.CreateCommentDto request, Long id, User user) throws IOException
    {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));
        Comments comments = CommentsConverter.toCommentsArticle(request);
        comments.setUser(user);
        comments.setArticle(article);
        article.countComments();
        if(!article.getUser().getId().equals(article.getUser().getId()))
            commentAlarmArticle(comments.getArticle(), comments, user, article.getUser());
        return commentsRepository.save(comments);
    }

    @Override
    @Transactional(readOnly = false)
    public Comments createOnRecord(CommentsRequestDto.CreateCommentDto request, Long id, User user) {
        Record record = recordRepository.findById(id).orElseThrow(()->new CommentsException(Code.RECORD_NOT_FOUND));
        Comments comments = CommentsConverter.toCommentsRecord(request);
        comments.setRecord(record);
        comments.setUser(user);
        record.countComments();
        return commentsRepository.save(comments);
    }

    @Override
    @Transactional(readOnly = false)
    public Comments updateOnArticle(CommentsRequestDto.UpdateCommentDto request, Long id, Long commentsId, User user) {
        articleRepository.findById(id).orElseThrow(() -> new ArticleException(Code.ARTICLE_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        if (!comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.COMMENTS_FORBIDDEN);
        Comments updatedComments = comments.setContents(request.getContents());
        return updatedComments;
    }

    @Override
    @Transactional(readOnly = false)
    public Comments updateOnRecord(CommentsRequestDto.UpdateCommentDto request, Long id, Long commentsId, User user) {
        recordRepository.findById(id).orElseThrow(() -> new ArticleException(Code.RECORD_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        if (!comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.COMMENTS_FORBIDDEN);
        Comments updatedComments = comments.setContents(request.getContents());
        return updatedComments;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteOnArticle(Long id, Long commentsId, User user) {
        articleRepository.findById(id).orElseThrow(()->new CommentsException(Code.ARTICLE_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        if (!comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.COMMENTS_FORBIDDEN);
        comments.getArticle().deleteComments();
        commentsRepository.delete(comments);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteOnRecord(Long id, Long commentsId, User user) {
        recordRepository.findById(id).orElseThrow(()->new CommentsException(Code.RECORD_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        if (!comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.COMMENTS_FORBIDDEN);
        comments.getRecord().deleteComments();
        commentsRepository.delete(comments);
    }

    @Override
    public Comments toggleCommentsLikeOnArticle(Long id, Long commentsId, User user) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        Optional<CommentsLikes> commentsLikesOptional = commentsLikesRepository.findByCommentsAndUser(comments, user);
        if (comments.getUser().equals(user))
            throw new CommentsException(Code.COMMENTS_LIKES_FORBBIDDEN);
        if(commentsLikesOptional.isPresent())
        {
            CommentsLikes commentsLikes = commentsLikesOptional.get();
            user.getCommentsLikesList().remove(commentsLikes);
            comments.getCommentsLikesList().remove(commentsLikes);
            commentsLikesRepository.delete(commentsLikes);
            comments.toggleLikes(true);
        }
        else{
            CommentsLikes newCommentsLikes = CommentsLikes.builder()
                    .comments(comments)
                    .user(user)
                    .build();
            user.getCommentsLikesList().add(newCommentsLikes);
            newCommentsLikes.setComments(comments);
            newCommentsLikes.setUser(user);
            commentsLikesRepository.save(newCommentsLikes);
            comments.toggleLikes(false);
        }
        return comments;
    }

    @Override
    public Comments toggleCommentsLikeOnRecord(Long id, Long commentsId, User user) {
        Record record= recordRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        Optional<CommentsLikes> commentsLikesOptional = commentsLikesRepository.findByCommentsAndUser(comments, user);
        if (comments.getUser().equals(user))
            throw new CommentsException(Code.COMMENTS_LIKES_FORBBIDDEN);
        if(commentsLikesOptional.isPresent())
        {
            CommentsLikes commentsLikes = commentsLikesOptional.get();
            user.getCommentsLikesList().remove(commentsLikes);
            comments.getCommentsLikesList().remove(commentsLikes);
            commentsLikesRepository.delete(commentsLikes);
            comments.toggleLikes(true);
        }
        else{
            CommentsLikes newCommentsLikes = CommentsLikes.builder()
                    .comments(comments)
                    .user(user)
                    .build();
            user.getCommentsLikesList().add(newCommentsLikes);
            newCommentsLikes.setComments(comments);
            newCommentsLikes.setUser(user);
            commentsLikesRepository.save(newCommentsLikes);
            comments.toggleLikes(false);
        }
        return comments;
    }

    @Override
    @Transactional
    public ContentsReport reportComments(Long commentsId, User user) {
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(()->new CommentsException(Code.COMMENT_NOT_FOUND));
        if(comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.MY_CONTENTS);
        Optional<ContentsReport> findReport = contentsReportRepository.findByUserAndComments(user, comments);
        if(findReport.isPresent())
            throw new CommentsException(Code.ALREADY_REPORT);
        User commentsOwner = userRepository.findById(comments.getUser().getId()).get();
        commentsOwner.countReport();
        comments.countReport();
        return contentsReportRepository.save(ContentsReport.builder()
                .contentsType(ContentsType.COMMENT)
                .comments(comments)
                .user(user)
                .build()
        );
    }

    @Override
    @Transactional
    public void commentAlarmArticle(Article article,Comments comments, User user, User owner) throws IOException
    {

        // 알림 테이블에 저장
        Notification notification = notificationRepository.save(Notification.builder()
                .notificationCategory(NotificationCategory.ARTICLE)
                .article(article)
                .user(owner)
                .notificationBody(user.getNickname().toString() + alarmBodyHad + article.getTitle() + alarmBodyMiddle + comments.getContents() + alarmBodyFoot)
                .isConfirmed(false)
                .build());

        notification.setUser(article.getUser());
        // 알림 보내기
        if(owner.getCommunityPermit())
            for(FcmToken fcmToken : owner.getFcmTokenList()){
                fireBaseService.sendMessageTo(fcmToken.getToken(),alarmTitle,user.getNickname().toString() + alarmBodyHad + article.getTitle() + alarmBodyMiddle + comments.getContents() + alarmBodyFoot, FCMType.ARTICLE.toString(),article.getId().toString(),notification.getId().toString());
            }
    }

    @Override
    @Transactional
    public void commentAlarmArticleApple(Article article,Comments comments, User user, User owner) throws IOException
    {

        // 알림 테이블에 저장
        Notification notification = notificationRepository.save(Notification.builder()
                .notificationCategory(NotificationCategory.ARTICLE)
                .article(article)
                .user(owner)
                .notificationBody(user.getNickname().toString() + alarmBodyHad + article.getTitle() + alarmBodyMiddle + comments.getContents() + alarmBodyFoot)
                .isConfirmed(false)
                .build());

        notification.setUser(article.getUser());
        // 알림 보내기
        if(owner.getCommunityPermit())
            for(FcmToken fcmToken : owner.getFcmTokenList()){
                fireBaseService.sendMessageToApple(fcmToken.getToken(),alarmTitle,user.getNickname().toString() + alarmBodyHad + article.getTitle() + alarmBodyMiddle + comments.getContents() + alarmBodyFoot, FCMType.ARTICLE.toString(),article.getId().toString(),notification.getId().toString());
            }
    }

    @Override
    @Transactional
    public void commentAlarmRecord(Record record, Comments comments,User user) throws IOException
    {
        // 알림 테이블에 저장
        Notification notification = notificationRepository.save(Notification.builder()
                .notificationCategory(NotificationCategory.RECORD)
                .record(record)
                .user(record.getUser())
                .notificationBody(user.getNickname().toString() + alarmRecordBodyHead +  comments.getContents() + alarmRecordBodyMiddle)
                .isConfirmed(false)
                .build());

        notification.setUser(record.getUser());

        // 알림 보내기
        if(record.getUser().getCommunityPermit())
            for(FcmToken fcmToken : record.getUser().getFcmTokenList()){
                fireBaseService.sendMessageToV2(fcmToken.getToken(),alarmTitle,user.getNickname().toString() + alarmRecordBodyHead +  comments.getContents() + alarmRecordBodyMiddle, FCMType.RECORD.toString(),record.getId().toString(),notification.getId().toString(),comments.getRecord().getImageUrl());
            }
    }

    @Override
    @Transactional
    public void commentAlarmRecordApple(Record record, Comments comments,User user) throws IOException
    {
        // 알림 테이블에 저장
        Notification notification = notificationRepository.save(Notification.builder()
                .notificationCategory(NotificationCategory.RECORD)
                .record(record)
                .user(record.getUser())
                .notificationBody(user.getNickname().toString() + alarmRecordBodyHead +  comments.getContents() + alarmRecordBodyMiddle)
                .isConfirmed(false)
                .build());

        notification.setUser(record.getUser());

        // 알림 보내기
        if(record.getUser().getCommunityPermit())
            for(FcmToken fcmToken : record.getUser().getFcmTokenList()){
                fireBaseService.sendMessageToAppleV2(fcmToken.getToken(),alarmTitle,user.getNickname().toString() + alarmRecordBodyHead +  comments.getContents() + alarmRecordBodyMiddle, FCMType.RECORD.toString(),record.getId().toString(),notification.getId().toString(),comments.getRecord().getImageUrl());
            }
    }
}
