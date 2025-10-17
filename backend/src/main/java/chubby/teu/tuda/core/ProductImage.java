package chubby.teu.tuda.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageId")
    private Integer imageId;

    @ManyToOne
    @JoinColumn(name = "productCode", nullable = false)
    private Product product;

    @Column(name = "imageUrl", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "isPrimary")
    private Boolean isPrimary = false;

    @Column(name = "createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();
}