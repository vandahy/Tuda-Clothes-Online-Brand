package chubby.teu.tuda.feature.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class OrderResponse {
    private String fullName;
    private String orderCode;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private Instant createdAt;

    private ShippingAddress shippingAddress;

    private List<OrderItemDTO> items;

    private PaymentInfo payment;

    private ShipmentInfo shipment;

    private String message;
    private String idempotencyKey;
    private Boolean cartCleared;

    @Data
    public static class OrderItemDTO {
        private Integer variantId;
        private String productCode;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal discount;
        private BigDecimal subTotal;
    }

    @Data
    public static class ShippingAddress {
        private String fullName;
        private String phone;
        private String street;
        private String city;
        private String postalCode;
        private String country;
    }

    @Data
    public static class PaymentInfo {
        private String method; // COD|BANK|MOMO|CARD|PAYPAL
        private String status; // PAID|UNPAID|PENDING|REFUNDED
        private BigDecimal amount;
        private String transactionId; // optional
        private String redirectUrl; // nếu cần redirect sang cổng thanh toán
        private String paymentInstructions;
    }

    @Data
    public static class ShipmentInfo {
        private String shipmentId;
        private String status; // PENDING|SHIPPING|DELIVERED|RETURNED
        private String shipperName;
        private String trackingNumber;
        private String estimatedDelivery; // ISO date or human readable
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPING, COMPLETED, CANCELLED
    }
}
