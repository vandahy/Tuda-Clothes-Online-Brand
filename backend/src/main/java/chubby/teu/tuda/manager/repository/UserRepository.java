package chubby.teu.tuda.manager.repository;

import chubby.teu.tuda.manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
