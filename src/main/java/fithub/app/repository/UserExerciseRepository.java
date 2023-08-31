package fithub.app.repository;

import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.User;
import fithub.app.domain.UserExercise;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserExerciseRepository extends JpaRepository<UserExercise, Long> {
    Optional<UserExercise> findByUserAndExerciseCategory(User user, ExerciseCategory exerciseCategory);
    List<UserExercise> findAllByUserAndExerciseCategory(User user, ExerciseCategory exerciseCategory);

    @Query("select ue from UserExercise ue where ue.user = :user and ue.exp > 0")
    List<UserExercise> findUserExercise(@Param("user") User user);

    @Query("select ue from UserExercise ue where ue.id in (select u.bestRecordExercise from User u) order by ue.records desc ")
    List<UserExercise> findTopFiveUserExercises(Pageable pageable);
}
