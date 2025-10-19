package chubby.teu.tuda.feature.cart.repository;

import chubby.teu.tuda.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Query()
    Optional<User> findByEmail(String email);
}
