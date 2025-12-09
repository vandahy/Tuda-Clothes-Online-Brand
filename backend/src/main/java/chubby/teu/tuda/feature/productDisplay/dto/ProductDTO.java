package chubby.teu.tuda.feature.productDisplay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String productCode;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer stockQuantity;
    private String categoryCode;
    private String categoryName; // lấy từ Category
    private CategoryDTO category; // Thêm category object đầy đủ
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductImageDTO> images; // Danh sách hình ảnh
}
