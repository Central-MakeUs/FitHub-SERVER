package fithub.app.redis.repository;

import fithub.app.redis.LoginStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LoginTokenRepository extends CrudRepository<LoginStatus, String> {

}
