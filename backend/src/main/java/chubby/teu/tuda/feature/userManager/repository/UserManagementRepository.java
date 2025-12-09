package chubby.teu.tuda.feature.userManager.repository;

import chubby.teu.tuda.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserManagementRepository extends JpaRepository<User, String> {
    List<User> findAllByOrderByCreatedAtDesc();
}
