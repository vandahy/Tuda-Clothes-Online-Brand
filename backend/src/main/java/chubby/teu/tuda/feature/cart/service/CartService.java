package chubby.teu.tuda.feature.cart.service;

import chubby.teu.tuda.core.Cart;
import chubby.teu.tuda.feature.cart.dto.AddToCartRequest;

public interface CartService {

    Cart addItemToCart(AddToCartRequest requestDTO, String username);

}
