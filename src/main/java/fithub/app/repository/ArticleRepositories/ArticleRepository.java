package fithub.app.repository.ArticleRepositories;

import fithub.app.domain.Article;
import fithub.app.domain.ExerciseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Article> findAllByExerciseCategoryOrderByCreatedAtDesc(ExerciseCategory exerciseCategory, Pageable pageable);
    Page<Article> findByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime createdAt, Pageable pageable);

    Page<Article> findByCreatedAtLessThanAndExerciseCategoryOrderByCreatedAtDesc(LocalDateTime createdAt, ExerciseCategory exerciseCategory, Pageable pageable);
}
