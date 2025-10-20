package chubby.teu.tuda.feature.cart.controller;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.cart.dto.AddToCartRequest;
import chubby.teu.tuda.feature.cart.repository.UserRepository;
import chubby.teu.tuda.feature.cart.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/carts")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    private UserRepository userRepository;

    //Add product to cart slidebar
    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody AddToCartRequest requestDTO, Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String username = user.get().getUsername();
        try {
            cartService.addItemToCart(requestDTO, username);
            // 4. Nếu thành công, trả về HTTP 200 OK cùng thông báo
            return ResponseEntity.ok(Map.of("message", "Successfully added product to cart"));
        } catch (EntityNotFoundException e) {
            // Nếu service ném ra lỗi không tìm thấy (user, product, size...)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            // Nếu service ném ra lỗi khác (ví dụ: hết hàng)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

}
