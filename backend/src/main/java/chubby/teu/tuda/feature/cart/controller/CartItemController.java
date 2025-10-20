package chubby.teu.tuda.feature.cart.controller;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.cart.dto.CartItemRequest;
import chubby.teu.tuda.feature.cart.repository.UserRepository;
import chubby.teu.tuda.feature.cart.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/slide-bar")
    public ResponseEntity<?> getCartItemsForSlideBar(Principal principal) {
        try {
            String email = principal.getName();
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return ResponseEntity.status(404).body("Không tìm thấy người dùng.");
            }

            List<CartItemRequest> items = cartItemService.selectCartItemToSlideBar(user.get().getUsername());
            return ResponseEntity.ok(items);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Integer variantId,
                                                 Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            cartItemService.removeItemFromCart(email, variantId);
            return ResponseEntity.ok("Successfully removed item from cart");
        } else {
            return ResponseEntity.status(401).body("Chưa xác thực"); // Hoặc 403
        }
    }

    @DeleteMapping("/clean")
    public ResponseEntity<String> removeAllFromCart(@RequestParam String cartCode) {
        cartItemService.removeAllItemsFromCart(cartCode);
        return ResponseEntity.ok("Successfully cleared your cart");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateQuantity(
            @RequestParam Integer variantId,
            @RequestParam int quantity,
            Authentication authentication) {

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String username = user.get().getUsername();
        cartItemService.updateCartItemQuantity(username, variantId, quantity);
        return ResponseEntity.ok("Successfully updated item quantity");
    }
}
