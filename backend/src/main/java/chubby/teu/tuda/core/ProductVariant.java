package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_variants",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_product_size",
                columnNames = {"productCode", "sizeId"}
        ))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variantId")
    private Integer variantId;

    @Column(name = "productCode", nullable = false, length = 10)
    private String productCode;

    @Column(name = "sizeId", nullable = false)
    private Integer sizeId;

    @Column(name = "stock", columnDefinition = "INT DEFAULT 0")
    private Integer stock = 0;

    @Column(name = "isActive", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    // Quan hệ Many-to-One với Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productCode", insertable = false, updatable = false)
    private Product product;

    // Quan hệ Many-to-One với Size
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sizeId", insertable = false, updatable = false)
    private Size size;

    // Constructor tiện lợi
    public ProductVariant(String productCode, Integer sizeId, Integer stock) {
        this.productCode = productCode;
        this.sizeId = sizeId;
        this.stock = stock;
        this.isActive = true;
    }
}
