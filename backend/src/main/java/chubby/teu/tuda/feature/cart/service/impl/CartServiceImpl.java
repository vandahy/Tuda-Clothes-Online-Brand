package chubby.teu.tuda.feature.cart.service.impl;

import chubby.teu.tuda.core.*;
import chubby.teu.tuda.feature.cart.dto.AddToCartRequest;
import chubby.teu.tuda.feature.cart.repository.*;
import chubby.teu.tuda.feature.cart.service.CartService;
import chubby.teu.tuda.feature.productDisplay.repository.ProductRepository;
import chubby.teu.tuda.feature.productDisplay.repository.ProductVariantRepository;
import chubby.teu.tuda.feature.productDisplay.repository.SizeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public Cart addItemToCart(AddToCartRequest requestDTO, String username) {
        // 1️.Tìm user và cart đang hoạt động
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng: " + username));

        Cart cart = cartRepository.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(user));

        // 2️.Tìm variant theo productCode + size
        Size size = sizeRepository.findByName(requestDTO.getSize())
                .orElseThrow(() -> new EntityNotFoundException("Size không hợp lệ: " + requestDTO.getSize()));

        ProductVariant variant = productVariantRepository
                .findByProduct_ProductCodeAndSizeId(requestDTO.getProductCode(), size.getSizeId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm với size đã chọn."));

        if (variant.getStock() < requestDTO.getQuantity()) {
            throw new RuntimeException("Sản phẩm không đủ số lượng tồn kho.");
        }

        // 3.Kiểm tra xem cart đã có variant đó chưa
        Optional<CartItem> existingItemOpt = cartItemRepository
                .findByCart_CartCodeAndVariant_VariantId(cart.getCartCode(), variant.getVariantId());

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();

            int newQuantity = existingItem.getQuantity() + requestDTO.getQuantity();
            existingItem.setQuantity(newQuantity);

            BigDecimal unitPrice = variant.getProduct().getPrice().subtract(variant.getProduct().getDiscount());
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(newQuantity));

            existingItem.setPrice(totalPrice);

            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setVariant(variant);
            newItem.setQuantity(requestDTO.getQuantity());

            BigDecimal unitPrice = variant.getProduct().getPrice().subtract(variant.getProduct().getDiscount());
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(requestDTO.getQuantity()));

            newItem.setPrice(totalPrice);

            cartItemRepository.save(newItem);
        }

        return cart;
    }


    private Cart createNewCart(User username) {
        Cart newCart = new Cart();
        int randomNumber = new Random().nextInt(1000);
        String formattedCode = String.format("%03d", randomNumber);
        newCart.setCartCode("CA" + formattedCode);
        newCart.setUser(username);
        newCart.setStatus(Cart.CartStatus.ACTIVE);
        return cartRepository.save(newCart);
    }
}
