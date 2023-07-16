package fithub.app.repository;

import fithub.app.domain.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid,Long> {
    boolean existsByUuid(String uuid);
}
