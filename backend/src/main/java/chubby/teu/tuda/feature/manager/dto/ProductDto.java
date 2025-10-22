
package chubby.teu.tuda.feature.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String productCode;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer stockQuantity;
    private LocalDateTime updatedAt;
    private CategoryDto category; // QUAN TRỌNG: Dùng object CategoryDto
}