package chubby.teu.tuda.feature.order.dto;

import chubby.teu.tuda.core.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderListRequest {
    private String orderCode;
    private LocalDateTime orderDate;
    private Order.OrderStatus orderStatus;
    private BigDecimal totalAmount;
}
