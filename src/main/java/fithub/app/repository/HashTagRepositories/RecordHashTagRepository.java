package fithub.app.repository.HashTagRepositories;

import fithub.app.domain.HashTag;
import fithub.app.domain.mapping.RecordHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordHashTagRepository extends JpaRepository<RecordHashTag, Long> {

    List<RecordHashTag> findAllByHashTag(HashTag hashTag);
}
