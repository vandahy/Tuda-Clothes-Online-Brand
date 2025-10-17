package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventoryId")
    private Integer inventoryId;

    @ManyToOne
    @JoinColumn(name = "productCode", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private InventoryType type;

    @Column(name = "quantityChange", nullable = false)
    private Integer quantityChange;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum InventoryType {
        IMPORT, EXPORT
    }
}