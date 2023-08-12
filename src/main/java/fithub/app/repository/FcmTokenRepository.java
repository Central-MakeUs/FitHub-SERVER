package fithub.app.repository;

import fithub.app.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
}
