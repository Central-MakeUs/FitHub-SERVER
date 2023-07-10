package fithub.app.repository;

import fithub.app.domain.PhoneAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {

    @Modifying
    @Query("delete from PhoneAuth p where p.phoneNum = :phoneNum")
    void deleteByPhoneNum(@Param("phoneNum") String phoneNum);

    @Query("select p from PhoneAuth p where p.phoneNum = :phoneNum")
    Optional<PhoneAuth> findByPhoneNum(@Param("phoneNum") String phoneNum);
}
