package chubby.teu.tuda.manager.service;

import chubby.teu.tuda.manager.model.Order;
import chubby.teu.tuda.manager.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
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
