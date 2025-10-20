package chubby.teu.tuda.feature.cart.service;

import chubby.teu.tuda.feature.cart.dto.CartItemRequest;

import java.util.List;

public interface CartItemService {
    List<CartItemRequest> selectCartItemToSlideBar(String username);
    void removeItemFromCart(String email, int variantId);
    void removeAllItemsFromCart(String cartCode);
    void updateCartItemQuantity(String username, int variantId, int newQuantity);
}
