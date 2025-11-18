package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderDetail.OrderDetailId.class)
public class OrderDetail {

    @Id
    @ManyToOne
    @JoinColumn(name = "orderCode", nullable = false)
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "productCode", nullable = false)
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "variantId", nullable = false)
    private ProductVariant variant;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unitPrice", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount", precision = 12, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "subTotal", precision = 14, scale = 2)
    private BigDecimal subTotal;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailId implements Serializable {
        private String order;
        private String product;
        private Integer variant;
    }
}