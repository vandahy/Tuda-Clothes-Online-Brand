package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString(exclude = {"images", "orderDetails", "inventories", "category"})
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(name = "productCode", length = 10)
    private String productCode;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "discount", precision = 12, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "stockQuantity")
    private Integer stockQuantity = 0;

    @ManyToOne
    @JoinColumn(name = "categoryCode", referencedColumnName = "categoryCode")
    private Category category;

    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Inventory> inventories;

    /**
     * Tự động đặt giá trị cho createdAt và updatedAt trước khi lưu lần đầu.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Tự động cập nhật giá trị cho updatedAt trước khi cập nhật bản ghi.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}