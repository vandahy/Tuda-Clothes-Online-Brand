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
@Table(name = "orders")
public class Order {

    @Id
    private String orderCode;

    // Quan hệ nhiều đơn hàng thuộc về 1 user
    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Column(insertable = false, updatable = false)
    private Timestamp orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(precision = 14, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(length = 255)
    private String shippingAddress;

    @Column(insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(insertable = false, updatable = false)
    private Timestamp updatedAt;

    public enum Status {
        PENDING, CONFIRMED, SHIPPING, COMPLETED, CANCELLED
    }
}
