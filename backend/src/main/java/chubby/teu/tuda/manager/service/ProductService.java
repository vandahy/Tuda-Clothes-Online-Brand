package chubby.teu.tuda.manager.service;

import chubby.teu.tuda.manager.model.Product;
import chubby.teu.tuda.manager.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product findById(String code) {
        return repo.findById(code).orElse(null);
    }

    public Product save(Product product) {
        return repo.save(product);
    }

    public void delete(String code) {
        repo.deleteById(code);
    }
}
