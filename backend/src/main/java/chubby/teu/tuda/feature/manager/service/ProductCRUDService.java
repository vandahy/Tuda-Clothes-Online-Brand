package chubby.teu.tuda.feature.manager.service;

import chubby.teu.tuda.core.Product;
import chubby.teu.tuda.feature.manager.repository.ProductCRUDRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCRUDService {

    private final ProductCRUDRepository repo;

    public ProductCRUDService(ProductCRUDRepository repo) {
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
