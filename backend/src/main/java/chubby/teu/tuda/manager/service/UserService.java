package chubby.teu.tuda.manager.service;

import chubby.teu.tuda.manager.model.User;
import chubby.teu.tuda.manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
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
