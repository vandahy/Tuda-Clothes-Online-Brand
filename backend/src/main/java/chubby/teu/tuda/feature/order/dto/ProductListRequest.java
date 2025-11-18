package chubby.teu.tuda.feature.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListRequest {
    private String productCode;
    private String productName;
    private String imageUrl;
    private String orderCode;
    private LocalDateTime orderDate;
    private int quantity;
    private BigDecimal price;
}