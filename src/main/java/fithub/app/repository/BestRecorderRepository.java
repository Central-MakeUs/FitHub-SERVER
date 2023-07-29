package fithub.app.repository;

import fithub.app.domain.BestRecorder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestRecorderRepository extends JpaRepository<BestRecorder, Long> {

}
