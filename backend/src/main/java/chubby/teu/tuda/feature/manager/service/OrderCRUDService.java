package chubby.teu.tuda.feature.manager.service;

import chubby.teu.tuda.core.Order;
import chubby.teu.tuda.feature.manager.repository.OrderCRUDRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderCRUDService {

    private final OrderCRUDRepository repo;

    public OrderCRUDService(OrderCRUDRepository repo) {
        this.repo = repo;
    }

    public List<Order> findAll() {
        return repo.findAll();
    }

    public Order findById(String code) {
        return repo.findById(code).orElse(null);
    }

    public Order save(Order order) {
        return repo.save(order);
    }

    public void delete(String code) {
        repo.deleteById(code);
    }
}
