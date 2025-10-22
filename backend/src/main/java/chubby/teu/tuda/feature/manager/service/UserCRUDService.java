package chubby.teu.tuda.feature.manager.service;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.manager.repository.UserCRUDRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCRUDService {

    private final UserCRUDRepository repo;

    public UserCRUDService(UserCRUDRepository repo) {
        this.repo = repo;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public User save(User user) {
        return repo.save(user);
    }

    public void delete(String username) {
        repo.deleteById(username);
    }

    public User findById(String username) {
        return repo.findById(username).orElse(null);
    }
}
