package chubby.teu.tuda.manager.controller;

import chubby.teu.tuda.manager.dto.OrderDto;
import chubby.teu.tuda.manager.model.Order;
import chubby.teu.tuda.manager.model.User;
import chubby.teu.tuda.manager.repository.OrderRepository;
import chubby.teu.tuda.manager.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    public OrderController(OrderRepository orderRepo, UserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(o -> new OrderDto(
                        o.getOrderCode(),
                        o.getUser().getUsername(),
                        o.getOrderDate(),
                        o.getStatus().name(),
                        o.getTotalAmount(),
                        o.getShippingAddress()
                ))
                .toList();
    }

    @PostMapping
    public OrderDto createOrder(@RequestBody OrderDto dto) {
        User user = userRepo.findById(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUsername()));

        Order order = new Order();
        order.setOrderCode(dto.getOrderCode());
        order.setUser(user);
        order.setStatus(Order.Status.valueOf(dto.getStatus()));
        order.setTotalAmount(dto.getTotalAmount());
        order.setShippingAddress(dto.getShippingAddress());

        Order saved = orderRepo.save(order);

        return new OrderDto(
                saved.getOrderCode(),
                saved.getUser().getUsername(),
                saved.getOrderDate(),
                saved.getStatus().name(),
                saved.getTotalAmount(),
                saved.getShippingAddress()
        );
    }

    @PutMapping("/{orderCode}")
    public OrderDto updateOrder(@PathVariable String orderCode, @RequestBody OrderDto dto) {
        Order existing = orderRepo.findById(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderCode));

        existing.setStatus(Order.Status.valueOf(dto.getStatus()));
        existing.setTotalAmount(dto.getTotalAmount());
        existing.setShippingAddress(dto.getShippingAddress());

        // Cập nhật user nếu có thay đổi
        if (dto.getUsername() != null) {
            User user = userRepo.findById(dto.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUsername()));
            existing.setUser(user);
        }

        Order updated = orderRepo.save(existing);

        return new OrderDto(
                updated.getOrderCode(),
                updated.getUser().getUsername(),
                updated.getOrderDate(),
                updated.getStatus().name(),
                updated.getTotalAmount(),
                updated.getShippingAddress()
        );
    }

    @DeleteMapping("/{orderCode}")
    public void deleteOrder(@PathVariable String orderCode) {
        orderRepo.deleteById(orderCode);
    }
}
