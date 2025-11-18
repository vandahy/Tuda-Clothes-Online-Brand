package chubby.teu.tuda.feature.order.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private String productName;
    private String imageUrl;
    private Integer quantity;
    private Double price;
    private String size;
    private int variantID;
    private Double subTotal;
}
