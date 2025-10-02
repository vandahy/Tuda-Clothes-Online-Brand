package chubby.teu.tuda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    @EmbeddedId
    private OrderDetailId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderCode") // ánh xạ với field orderCode trong OrderDetailId
    @JoinColumn(name = "orderCode", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productCode") // ánh xạ với field productCode trong OrderDetailId
    @JoinColumn(name = "productCode", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unitPrice", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount", precision = 12, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "subTotal", precision = 14, scale = 2)
    private BigDecimal subTotal;

    // Embedded composite key class
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailId {
        @Column(name = "orderCode")
        private String orderCode;

        @Column(name = "productCode")
        private String productCode;
    }
}

