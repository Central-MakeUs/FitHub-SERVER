package fithub.app.repository;

import fithub.app.domain.BestRecorder;
import fithub.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BestRecorderRepository extends JpaRepository<BestRecorder, Long> {

    Optional<BestRecorder> findByUserId(Long userId);
}
