package fithub.app.repository.HashTagRepositories;

import fithub.app.domain.HashTag;
import fithub.app.domain.User;
import fithub.app.domain.mapping.RecordHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordHashTagRepository extends JpaRepository<RecordHashTag, Long> {

    @Query("select rh from RecordHashTag rh where rh.hashTag = :hashTag and rh.record.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and rh.record.user not in (select ur.reporter from UserReport  ur where ur.user = :target)")
    List<RecordHashTag> findAllByHashTag(@Param("hashTag") HashTag hashTag, @Param("reporter") User reporter, @Param("target") User target);
}
