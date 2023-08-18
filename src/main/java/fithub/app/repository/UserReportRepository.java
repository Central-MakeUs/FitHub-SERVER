package fithub.app.repository;

import fithub.app.domain.User;
import fithub.app.domain.mapping.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    Optional<UserReport> findByUserAndReporter(User target, User reporter);

    @Query("SELECT COUNT(ur) FROM UserReport ur WHERE (ur.user = :target AND ur.reporter = :reporter) OR (ur.reporter = :target AND ur.user = :reporter)")
    Long checkReport (@Param("reporter") User reporter, @Param("target") User target);

    Optional<UserReport> findByReporter(User user);

    Optional<UserReport> findByReporterAndUser(User reporter, User target);
}
