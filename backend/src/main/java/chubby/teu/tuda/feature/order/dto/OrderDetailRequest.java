package chubby.teu.tuda.feature.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDetailRequest {
    // Order info
    private String orderCode;
    private LocalDateTime orderDate;
    private String orderStatus;

    // Customer info
    private String customerName;
    private String customerPhone;
    private String customerAddress;

    // Payment info
    private String paymentMethod;
    private String paymentStatus;

    // Order totals
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;

    // Order items
    private List<OrderItemRequest> items;

    // Constructor with all 13 fields (including items)
    public OrderDetailRequest(String orderCode, LocalDateTime orderDate, String orderStatus,
            String customerName, String customerPhone, String customerAddress,
            String paymentMethod, String paymentStatus,
            BigDecimal subtotal, BigDecimal shippingFee,
            BigDecimal discountAmount, BigDecimal totalAmount,
            List<OrderItemRequest> items) {
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    // Constructor with 12 fields (without items - for JPQL query)
    public OrderDetailRequest(String orderCode, LocalDateTime orderDate, String orderStatus,
            String customerName, String customerPhone, String customerAddress,
            String paymentMethod, String paymentStatus,
            BigDecimal subtotal, BigDecimal shippingFee,
            BigDecimal discountAmount, BigDecimal totalAmount) {
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.items = null;
    }
}
