package fithub.app.repository.RecordRepositories;

import fithub.app.domain.Article;
import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Page<Record> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Record> findAllByExerciseCategoryOrderByCreatedAtDesc(ExerciseCategory exerciseCategory, Pageable pageable);
    Page<Record> findByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime createdAt, Pageable pageable);
    Page<Record> findByCreatedAtLessThanAndExerciseCategoryOrderByCreatedAtDesc(LocalDateTime createdAt, ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Record> findAllByOrderByLikesDesc(Pageable pageable);

    Page<Record> findAllByExerciseCategoryOrderByLikesDesc(ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Record> findByLikesLessThanOrderByLikesDesc(Long likes, Pageable pageable);

    Page<Record> findByLikesLessThanAndExerciseCategoryOrderByLikesDesc(Long likes, ExerciseCategory exerciseCategory, Pageable pageable);

    Page<Record> findByIdInAndCreatedAtLessThanOrderByCreatedAtDesc(List<Long> recordIds, LocalDateTime createdAt, Pageable pageable);
    Page<Record> findByIdInOrderByCreatedAtDesc(List<Long> recordIds,Pageable pageable);

    Page<Record> findByIdInAndLikesLessThanOrderByLikesDesc(List<Long> articleIds, Long likes, Pageable pageable);
    Page<Record> findByIdInOrderByLikesDesc(List<Long> articleIds, Pageable pageable);

    Page<Record> findByCreatedAtLessThanAndUserOrderByCreatedAtDesc(LocalDateTime createdAt, User user, Pageable pageable);
    Page<Record> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Record> findByCreatedAtLessThanAndUserAndExerciseCategoryOrderByCreatedAtDesc(LocalDateTime createdAt, User user, ExerciseCategory exerciseCategory,Pageable pageable);
    Page<Record> findAllByUserAndExerciseCategoryOrderByCreatedAtDesc(User user, ExerciseCategory exerciseCategory,Pageable pageable);

    List<Record> findByIdIn(List<Long> recordIdList);
}
