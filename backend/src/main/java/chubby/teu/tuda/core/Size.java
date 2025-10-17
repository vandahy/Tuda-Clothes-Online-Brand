package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "sizes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sizeId")
    private Integer sizeId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    // Quan hệ 1-n với ProductVariant
    @OneToMany(mappedBy = "size", cascade = CascadeType.ALL)
    private List<ProductVariant> productVariants;

    // Constructor tiện lợi (không bao gồm productVariants để tránh circular reference)
    public Size(String name) {
        this.name = name;
    }
}
