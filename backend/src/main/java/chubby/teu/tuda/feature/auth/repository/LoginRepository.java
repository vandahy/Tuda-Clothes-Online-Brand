package chubby.teu.tuda.feature.auth.repository;

import chubby.teu.tuda.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmailOrPhone(String username, String email, String phone);
}
