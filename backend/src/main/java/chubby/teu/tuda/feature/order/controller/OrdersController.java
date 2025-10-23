package chubby.teu.tuda.feature.order.controller;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.cart.repository.UserRepository;
import chubby.teu.tuda.feature.order.dto.OrderListRequest;
import chubby.teu.tuda.feature.order.dto.OrderResponse;
import chubby.teu.tuda.feature.order.dto.PlaceOrderRequest;
import chubby.teu.tuda.feature.order.dto.OrderDetailRequest;
import chubby.teu.tuda.feature.order.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private final OrdersService ordersService;

    @Autowired
    UserRepository userRepository;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest req, Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        req.setUsername(user.get().getUsername());

        OrderResponse resp = ordersService.placeOrder(req);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("list")
    public ResponseEntity<List<OrderListRequest>> getOrders(Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String username = user.get().getUsername();
        List<OrderListRequest> orders = ordersService.getOrderList(username);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<OrderDetailRequest> getOrderDetail(@PathVariable String orderCode) {
        OrderDetailRequest detail = ordersService.getOrderDetail(orderCode);
        return ResponseEntity.ok(detail);
    }
}
