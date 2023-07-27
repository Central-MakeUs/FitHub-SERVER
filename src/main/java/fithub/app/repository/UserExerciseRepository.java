package fithub.app.repository;

import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.User;
import fithub.app.domain.UserExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserExerciseRepository extends JpaRepository<UserExercise, Long> {
    Optional<UserExercise> findByUserAndExerciseCategory(User user, ExerciseCategory exerciseCategory);
}
