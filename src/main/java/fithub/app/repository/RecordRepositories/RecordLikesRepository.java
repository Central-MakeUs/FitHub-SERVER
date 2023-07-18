package fithub.app.repository.RecordRepositories;

import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.mapping.RecordLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordLikesRepository extends JpaRepository<RecordLikes, Long> {

    Optional<RecordLikes> findByRecordAndUser(Record record, User user);
}
