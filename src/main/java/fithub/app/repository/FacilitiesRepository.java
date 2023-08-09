package fithub.app.repository;

import fithub.app.domain.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilitiesRepository extends JpaRepository<Facilities, Long> {
}
