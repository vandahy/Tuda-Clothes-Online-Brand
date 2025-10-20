package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentId")
    private Integer paymentId;

    @OneToOne
    @JoinColumn(name = "orderCode", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethod method = PaymentMethod.COD;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Column(name = "transactionId", length = 100)
    private String transactionId;

    @Column(name = "paymentDate")
    private LocalDateTime paymentDate = LocalDateTime.now();

    public enum PaymentMethod {
        COD, BANK, MOMO
    }

    public enum PaymentStatus {
        PAID, UNPAID, REFUNDED
    }
}