package fithub.app.repository.RecordRepositories;

import fithub.app.domain.Article;
import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ContentsReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("select r from Record r where r.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and r.user.id not in (select ur.reporter.id from UserReport ur where ur.user =:target) order by r.createdAt desc ")
    Page<Record> findAllByOrderByCreatedAtDesc(@Param("reporter") User reporter, @Param("target") User target,Pageable pageable);

    @Query("select r from Record r where r.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and r.user.id not in (select ur.reporter.id from UserReport ur where ur.user =:target) and r.exerciseCategory = :category order by r.createdAt desc ")
    Page<Record> findAllByExerciseCategoryOrderByCreatedAtDesc(@Param("category") ExerciseCategory category,@Param("reporter") User reporter ,@Param("target") User target,Pageable pageable);

    @Query("select r from Record r where r.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and r.user.id not in (select ur.reporter.id from UserReport ur where ur.user =:target) order by r.likes desc, r.createdAt desc ")
    Page<Record> findByOrderByLikesDescCreatedAtDesc(@Param("reporter") User reporter,@Param("target") User target,Pageable pageable);

    @Query("select r from Record r where r.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and r.user.id not in (select ur.reporter.id from UserReport ur where ur.user =:target) and r.exerciseCategory = :category order by r.likes desc, r.createdAt desc ")
    Page<Record> findByExerciseCategoryOrderByLikesDescCreatedAtDesc(@Param("category") ExerciseCategory category, @Param("reporter") User reporter,@Param("target") User target,Pageable pageable);

    Page<Record> findByIdInAndCreatedAtLessThanOrderByCreatedAtDesc(List<Long> recordIds, LocalDateTime createdAt, Pageable pageable);
    Page<Record> findByIdInOrderByCreatedAtDesc(List<Long> recordIds,Pageable pageable);

    Page<Record> findByIdInOrderByLikesDescCreatedAtDesc(List<Long> articleIds, Pageable pageable);

    Page<Record> findByCreatedAtLessThanAndUserOrderByCreatedAtDesc(LocalDateTime createdAt, User user, Pageable pageable);
    Page<Record> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Record> findByCreatedAtLessThanAndUserAndExerciseCategoryOrderByCreatedAtDesc(LocalDateTime createdAt, User user, ExerciseCategory exerciseCategory,Pageable pageable);
    Page<Record> findAllByUserAndExerciseCategoryOrderByCreatedAtDesc(User user, ExerciseCategory exerciseCategory,Pageable pageable);

    List<Record> findByIdIn(List<Long> recordIdList);
}
