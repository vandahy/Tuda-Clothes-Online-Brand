package chubby.teu.tuda.feature.order.controller;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.cart.repository.UserRepository;
import chubby.teu.tuda.feature.order.dto.OrderDetailRequest;
import chubby.teu.tuda.feature.order.dto.ProductListRequest;
import chubby.teu.tuda.feature.order.service.OrdersDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-detail")
public class OrderDetailController {
    @Autowired
    OrdersDetailService ordersDetailService;

    @Autowired
    private UserRepository userRepository;

//    @GetMapping
//    public ResponseEntity<OrderDetailRequest> orderDetail(@PathVariable String codeOrder) {
//        OrderDetailRequest orderDetail = ordersDetailService.getOrderDetail(codeOrder);
//        return ResponseEntity.ok(orderDetail);
//    }

    @GetMapping("/product-list")
    public ResponseEntity<List<ProductListRequest>> getMyPurchasedItems(Principal principal) {

        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String username = user.get().getUsername();

        List<ProductListRequest> items = ordersDetailService.getAllPurchasedItems(username);

        return ResponseEntity.ok(items);
    }
}
