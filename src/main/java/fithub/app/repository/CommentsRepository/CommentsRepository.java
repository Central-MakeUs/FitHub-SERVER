package fithub.app.repository.CommentsRepository;

import fithub.app.domain.Article;
import fithub.app.domain.Comments;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ContentsReport;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    Page<Comments> findByCreatedAtLessThanAndArticleOrderByCreatedAtDesc(LocalDateTime createdAt, Article article, Pageable pageable);

    @Query("select c from Comments c where c.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and c.user.id not in (select ur.reporter from UserReport ur where ur.user = :target) and c.article = :article order by c.createdAt desc ")
    Page<Comments> findByArticleOrderByCreatedAtDesc(@Param("article") Article article, @Param("reporter") User reporter,@Param("target") User target,Pageable pageable);

    Page<Comments> findByCreatedAtLessThanAndRecordOrderByCreatedAtDesc(LocalDateTime createdAt, Record record, Pageable pageable);

    @Query("select c from Comments c where c.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and c.user.id not in (select ur.reporter from UserReport ur where ur.user = :target) and c.record = :record order by c.createdAt desc ")
    Page<Comments> findByRecordOrderByCreatedAtDesc(@Param("record") Record record, @Param("reporter") User reporter,@Param("target") User target,Pageable pageable);
    Optional<Comments> findByIdAndIsRecord(Long id, Boolean isRecord);
}
