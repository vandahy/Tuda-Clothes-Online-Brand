package chubby.teu.tuda.feature.order.controller;

import chubby.teu.tuda.core.*;
import chubby.teu.tuda.feature.cart.repository.CartItemRepository;
import chubby.teu.tuda.feature.cart.repository.CartRepository;
import chubby.teu.tuda.feature.cart.repository.UserRepository;
import chubby.teu.tuda.feature.order.dto.CartItemRequest;
import chubby.teu.tuda.feature.order.dto.CheckOutRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class CheckOutController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/check-out")
    public CheckOutRequest checkOut(Principal principal) {
        // Lấy user
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String username = user.get().getUsername();

        // Lấy cart hiện tại
        Optional<Cart> cartOpt = cartRepository.findActiveCartByUsername(username);
        Cart cart = cartOpt.orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItemRequest> cartItems = cartItemRepository.getItems(cart.getCartCode())
                .stream().map(item -> {
                    CartItemRequest dto = new CartItemRequest();
                    Product product = item.getVariant().getProduct();
                    dto.setProductName(item.getVariant().getProduct().getName());
                    dto.setSize(item.getVariant().getSize().getName());
                    dto.setQuantity(item.getQuantity());
                    dto.setPrice(item.getPrice().doubleValue());
                    dto.setSubTotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())).doubleValue());
                    String imageUrl = "DEFAULT_IMAGE";
                    if (product.getImages() != null && !product.getImages().isEmpty()) {
                        imageUrl = product.getImages().stream()
                                .filter(pi -> Boolean.TRUE.equals(pi.getIsPrimary()))
                                .map(ProductImage::getImageUrl)
                                .findFirst()
                                .orElseGet(() ->
                                        product.getImages().stream()
                                                .findFirst()
                                                .map(ProductImage::getImageUrl)
                                                .orElse("DEFAULT_IMAGE")
                                );
                    }
                    dto.setImageUrl(imageUrl);
                    dto.setVariantID(item.getVariant().getVariantId().intValue());
                    return dto;
                }).toList();

        // Tạo DTO chính
        CheckOutRequest checkoutDTO = new CheckOutRequest();
        checkoutDTO.setFirstName(user.get().getFullName());
        checkoutDTO.setEmail(user.get().getEmail());
        checkoutDTO.setPhone(user.get().getPhone());
        checkoutDTO.setCartCode(cart.getCartCode());
        checkoutDTO.setItems(cartItems);

        String fullAddress = user.get().getAddress();

        if (fullAddress != null && !fullAddress.isEmpty()) {
            String[] parts = fullAddress.split(", ");

            if (parts.length == 4) {
                checkoutDTO.setAddress(parts[0]);
                checkoutDTO.setWard(parts[1]);
                checkoutDTO.setDistrict(parts[2]);
                checkoutDTO.setCity(parts[3]);
            } else {
                // Trường hợp dữ liệu rác/sai: dồn tất cả vào ô số nhà
                checkoutDTO.setAddress(fullAddress);
            }
        }

        return checkoutDTO;
    }
}
