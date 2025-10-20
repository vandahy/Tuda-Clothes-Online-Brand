package chubby.teu.tuda.manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    private String productCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(precision = 12, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    private Integer stockQuantity = 0;

    // Khóa ngoại sang Category
    @ManyToOne
    @JoinColumn(name = "categoryCode")
    private Category category;

    // DB tự động set thời gian
    @Column(insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(insertable = false, updatable = false)
    private Timestamp updatedAt;
}
