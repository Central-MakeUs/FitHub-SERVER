package fithub.app.repository.ArticleRepositories;

import fithub.app.domain.Article;
import fithub.app.domain.ExerciseCategory;
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
    Page<Article> findByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime createdAt, Pageable pageable);
    Page<Article> findByCreatedAtLessThanAndExerciseCategoryOrderByCreatedAtDesc(LocalDateTime createdAt, ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Article> findAllByOrderByLikesDesc(Pageable pageable);

    Page<Article> findAllByExerciseCategoryOrderByLikesDesc(ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Article> findByLikesLessThanOrderByLikesDesc(Long likes, Pageable pageable);

    Page<Article> findByLikesLessThanAndExerciseCategoryOrderByLikesDesc(Long likes, ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Article> findByIdInAndCreatedAtLessThanOrderByCreatedAtDesc(List<Long> articleIds, LocalDateTime createdAt, Pageable pageable);
    Page<Article> findByIdInOrderByCreatedAtDesc(List<Long> articleIds, Pageable pageable);
    Page<Article> findByIdInAndLikesLessThanOrderByCreatedAtDesc(List<Long> articleIds, Long likes, Pageable pageable);
    Page<Article> findByIdInOrderByLikesDesc(List<Long> articleIds, Pageable pageable);
}
