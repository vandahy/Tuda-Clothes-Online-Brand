package chubby.teu.tuda.feature.manager.service;

import chubby.teu.tuda.core.Category;
import chubby.teu.tuda.feature.manager.repository.CategoryCRUDRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryCRUDService {

    private final CategoryCRUDRepository repo;

    public CategoryCRUDService(CategoryCRUDRepository repo) {
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
