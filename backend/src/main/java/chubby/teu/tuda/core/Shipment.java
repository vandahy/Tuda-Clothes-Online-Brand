package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipmentId")
    private Integer shipmentId;

    @OneToOne
    @JoinColumn(name = "orderCode", nullable = false, unique = true)
    private Order order;

    @Column(name = "shipperName", length = 100)
    private String shipperName;

    @Column(name = "trackingNumber", length = 100)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ShipmentStatus status = ShipmentStatus.PENDING;

    @Column(name = "deliveryDate")
    private LocalDate deliveryDate;

    public enum ShipmentStatus {
        PENDING, SHIPPING, DELIVERED, RETURNED
    }
}