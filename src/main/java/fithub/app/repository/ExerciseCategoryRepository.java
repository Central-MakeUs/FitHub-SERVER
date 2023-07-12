package fithub.app.repository;

import fithub.app.domain.ExerciseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseCategoryRepository extends JpaRepository<ExerciseCategory, Integer> {

    List<ExerciseCategory> findAll();
}
