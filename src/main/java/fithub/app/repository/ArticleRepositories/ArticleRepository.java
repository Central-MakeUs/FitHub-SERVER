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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Article> findAllByExerciseCategoryOrderByCreatedAtDesc(ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Article> findAllByOrderByLikesDescCreatedAtDesc(Pageable pageable);

    Page<Article> findAllByExerciseCategoryOrderByLikesDescCreatedAtDesc(ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Article> findByIdInAndCreatedAtLessThanOrderByCreatedAtDesc(List<Long> articleIds, LocalDateTime createdAt, Pageable pageable);
    Page<Article> findByIdInOrderByCreatedAtDesc(List<Long> articleIds, Pageable pageable);
    Page<Article> findByIdInOrderByLikesDescCreatedAtDesc(List<Long> articleIds, Pageable pageable);

    Page<Article> findByCreatedAtLessThanAndUserOrderByCreatedAtDesc(LocalDateTime createdAt, User user, Pageable pageable);

    Page<Article> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Article> findByCreatedAtLessThanAndUserAndExerciseCategoryOrderByCreatedAtDesc(LocalDateTime createdAt, User user,ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Article> findAllByUserAndExerciseCategoryOrderByCreatedAtDesc(User user, ExerciseCategory exerciseCategory,Pageable pageable);

    List<Article> findByIdIn(List<Long> articleList);
    void deleteAllByIdInBatch(Iterable<Long> artilceIdList);
}
