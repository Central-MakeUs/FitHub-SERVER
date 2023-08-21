package fithub.app.repository;

import fithub.app.domain.Terms;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Log> {
}
