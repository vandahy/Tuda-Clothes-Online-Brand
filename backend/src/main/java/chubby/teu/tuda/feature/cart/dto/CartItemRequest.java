package chubby.teu.tuda.feature.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemRequest {
    private String cartCode;
    private String productName;
    private String imageUrl;
    private Integer quantity;
    private Double price;
    private String size;
    private Integer variantId;
}
