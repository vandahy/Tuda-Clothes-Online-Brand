package chubby.teu.tuda.feature.cart.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private String productCode;
    private int quantity;
    private String size;
}
