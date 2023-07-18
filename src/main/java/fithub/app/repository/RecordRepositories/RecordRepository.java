package fithub.app.repository.RecordRepositories;

import fithub.app.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
