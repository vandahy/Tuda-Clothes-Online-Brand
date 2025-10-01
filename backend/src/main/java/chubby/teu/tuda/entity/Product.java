package chubby.teu.tuda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
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
    
    @Column(name = "imageUrl", length = 255)
    private String imageUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryCode")
    private Category category;
    
    @Column(name = "createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;
}
