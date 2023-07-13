package fithub.app.repository;

import fithub.app.domain.mapping.ExercisePreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExercisePreferenceRepository extends JpaRepository<ExercisePreference, Long> {
}
