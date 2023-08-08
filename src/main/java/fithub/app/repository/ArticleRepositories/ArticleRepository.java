package fithub.app.repository.ArticleRepositories;

import fithub.app.domain.Article;
import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.User;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a where a.user.id not in (select us.user.id from UserReport us where us.reporter = :reporter) and a.user.id not in (select ur.reporter.id from UserReport ur where ur.user =:target) order by a.createdAt desc")
    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable, @Param("reporter") User reporter, @Param("target") User target);
    @Query("select a from Article a where a.user.id not in (select us.user.id from UserReport us where us.reporter = :reporter) and a.user.id not in (select ur.reporter.id from UserReport ur where ur.user =:target) and a.exerciseCategory = :category order by a.createdAt desc ")
    Page<Article> findAllByExerciseCategoryOrderByCreatedAtDesc(@Param("category") ExerciseCategory exerciseCategory, @Param("reporter") User reporter,@Param("target") User target,Pageable pageable);

    @Query("select a from Article a where a.user.id not in (select us.user.id from UserReport us where us.reporter = :reporter) and a.user.id not in (select ur.reporter.id from UserReport ur where ur.user =:target) order by a.likes desc , a.createdAt desc ")
    Page<Article> findAllByOrderByLikesDescCreatedAtDesc(Pageable pageable, @Param("reporter") User reporter,@Param("target") User target);

    @Query("select a from Article a where a.user.id not in (select us.user.id from UserReport us where us.reporter = :reporter ) and a.user.id not in (select ur.reporter.id from UserReport ur where ur.user =:target) and a.exerciseCategory = :category order by a.likes desc , a.createdAt desc ")
    Page<Article> findAllByExerciseCategoryOrderByLikesDescCreatedAtDesc(@Param("category") ExerciseCategory category,@Param("reporter") User reporter,@Param("target") User target, Pageable pageable);

    @Query("select a from Article a where a in (select sa.article from SavedArticle sa where sa.user = :owner) and a.user.id not in (select us.user.id from UserReport us where us.reporter = :reporter) and a.exerciseCategory = :category order by a.likes desc , a.createdAt desc ")
    Page<Article> findAllSavedArticleCategory(@Param("owner") User owner, @Param("reporter") User reporter, @Param("category") ExerciseCategory category, Pageable pageable);

    @Query("select a from Article a where a in (select sa.article from SavedArticle sa where sa.user = :owner) and a.user.id not in (select us.user.id from UserReport us where us.reporter = :reporter) order by a.likes desc, a.createdAt desc")
    Page<Article> findAllSavedArticle(@Param("owner") User owner, @Param("reporter") User reporter, Pageable pageable);

    Page<Article> findByIdInAndCreatedAtLessThanOrderByCreatedAtDesc(List<Long> articleIds, LocalDateTime createdAt, Pageable pageable);
    Page<Article> findByIdInOrderByCreatedAtDesc(List<Long> articleIds, Pageable pageable);
    Page<Article> findByIdInOrderByLikesDescCreatedAtDesc(List<Long> articleIds, Pageable pageable);

    Page<Article> findByCreatedAtLessThanAndUserOrderByCreatedAtDesc(LocalDateTime createdAt, User user, Pageable pageable);

    Page<Article> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Article> findByCreatedAtLessThanAndUserAndExerciseCategoryOrderByCreatedAtDesc(LocalDateTime createdAt, User user,ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Article> findAllByUserAndExerciseCategoryOrderByCreatedAtDesc(User user, ExerciseCategory exerciseCategory,Pageable pageable);

    List<Article> findByIdIn(List<Long> articleList);
    void deleteAllByIdInBatch(Iterable<Long> artilceIdList);

    @Query("select count(al) from ArticleLikes al where al.article = :article and al.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and al.user not in (select ur.reporter from UserReport ur where ur.user = :target)")
    Long countLikes(@Param("article") Article article, @Param("reporter") User reporter, @Param("target") User target);

    @Query("select count(c) from Comments c where c.article = :article and c.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and c.user not in (select ur.reporter from UserReport ur where ur.user = :target)")
    Long countComments(@Param("article") Article article, @Param("reporter") User reporter, @Param("target") User target);

    @Query("select count (sa) from SavedArticle sa where sa.article = :article and sa.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and sa.user not in (select ur.reporter from UserReport ur where ur.user = :target)")
    Long countScraps(@Param("article") Article article, @Param("reporter") User reporter, @Param("target") User target);
}
