package chubby.teu.tuda.feature.orderManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private String productCode;
    private String productName;
    private Integer variantId;
    private String sizeName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal subTotal;
}
