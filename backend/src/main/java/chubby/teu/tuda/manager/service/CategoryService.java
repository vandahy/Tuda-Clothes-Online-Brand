package chubby.teu.tuda.manager.service;

import chubby.teu.tuda.manager.model.Category;
import chubby.teu.tuda.manager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> findAll() {
        return repo.findAll();
    }

    public Category findById(String code) {
        return repo.findById(code).orElse(null);
    }

    public Category save(Category category) {
        return repo.save(category);
    }

    public void delete(String code) {
        repo.deleteById(code);
    }
}
