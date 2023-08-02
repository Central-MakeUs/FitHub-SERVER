package fithub.app.repository;

import fithub.app.domain.User;
import fithub.app.domain.mapping.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    Optional<UserReport> findByUserAndReporter(User target, User reporter);
}
