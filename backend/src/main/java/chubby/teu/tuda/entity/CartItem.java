package chubby.teu.tuda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    
    @EmbeddedId
    private CartItemId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartCode") // liên kết với field cartCode trong CartItemId
    @JoinColumn(name = "cartCode", nullable = false)
    private Cart cart;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productCode") // liên kết với field productCode trong CartItemId
    @JoinColumn(name = "productCode", nullable = false)
    private Product product;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;
    
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    
    // Embedded composite key class
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemId {
        @Column(name = "cartCode")
        private String cartCode;
        
        @Column(name = "productCode")
        private String productCode;
    }
}
