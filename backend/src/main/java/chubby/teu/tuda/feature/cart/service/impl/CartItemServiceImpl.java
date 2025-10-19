package chubby.teu.tuda.feature.cart.service.impl;

import chubby.teu.tuda.core.*;
import chubby.teu.tuda.feature.cart.dto.CartItemRequest;
import chubby.teu.tuda.feature.cart.repository.CartItemRepository;
import chubby.teu.tuda.feature.cart.repository.CartRepository;
import chubby.teu.tuda.feature.cart.repository.UserRepository;
import chubby.teu.tuda.feature.cart.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    //Lấy danh sách item trong giỏ để hiển thị ở slide bar
    @Override
    @Transactional(readOnly = true)
    public List<CartItemRequest> selectCartItemToSlideBar(String username) {
        Optional<Cart> cartActive = cartRepository.findActiveCartByUsername(username);

        if (cartActive.isEmpty()) {
            return Collections.emptyList();
        }

        Cart cart = cartActive.get();
        List<CartItem> items = cartItemRepository.findByCart(cart);

        List<CartItemRequest> dtos = new ArrayList<>();

        for (CartItem item : items) {
            ProductVariant variant = item.getVariant();
            Product product = item.getVariant().getProduct();

            String imageUrl = findPrimaryImageUrl(product.getImages());
            BigDecimal unitPrice = product.getPrice().subtract(product.getDiscount());
            BigDecimal lineTotalPrice = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            double priceAsDouble = lineTotalPrice.doubleValue();

            String sizeName = variant.getSize().getName();

            CartItemRequest dto = new CartItemRequest(
                    item.getCart().getCartCode(),
                    product.getName(),
                    imageUrl,
                    item.getQuantity(),
                    priceAsDouble,
                    sizeName,
                    item.getVariant().getVariantId()
            );

            dtos.add(dto);
        }
        return dtos;
    }

    private String findPrimaryImageUrl(List<ProductImage> images) {
        if (images == null || images.isEmpty()) {
            return "/default-image.jpg";
        }

        return images.stream()
                .filter(ProductImage::getIsPrimary)
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(images.get(0).getImageUrl());
    }

    // Xóa 1 sản phẩm khỏi giỏ (dựa theo variantId)
    @Override
    @Transactional
    public void removeItemFromCart(String email, int variantId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng: " + email));

        Cart cart = cartRepository.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giỏ hàng đang hoạt động."));

        cartItemRepository.deleteByCartCodeAndVariantId(cart.getCartCode(), variantId);
    }

    //Xóa toàn bộ sản phẩm trong giỏ
    @Override
    @Transactional
    public void removeAllItemsFromCart(String cartCode) {
        cartItemRepository.deleteByCart_CartCode(cartCode);
    }

    // Cập nhật số lượng 1 item trong giỏ
    @Transactional
    public void updateCartItemQuantity(String username, int variantId, int newQuantity) {
        //Tìm cart của user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user " + username));

        Cart cart = cartRepository.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giỏ hàng của user"));

        //Tìm cart-item tương ứng
        CartItem item = cartItemRepository
                .findByCart_CartCodeAndVariant_VariantId(cart.getCartCode(), variantId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));

        //Cập nhật số lượng và giá
        item.setQuantity(newQuantity);
        BigDecimal unitPrice = item.getVariant().getProduct().getPrice()
                .subtract(item.getVariant().getProduct().getDiscount());
        item.setPrice(unitPrice.multiply(BigDecimal.valueOf(newQuantity)));

        cartItemRepository.save(item);
    }
}
