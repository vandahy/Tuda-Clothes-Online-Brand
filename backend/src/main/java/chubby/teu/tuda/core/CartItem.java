package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CartItem.CartItemId.class)
public class CartItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "cartCode", nullable = false)
    private Cart cart;

    @Id
    @ManyToOne
    @JoinColumn(name = "productCode", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemId implements Serializable {
        private String cart;
        private String product;
    }
}