package chubby.teu.tuda.feature.manager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {
    private String orderCode;
    private String username;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    private String shippingAddress;

    public OrderDto(String orderCode, String username, LocalDateTime orderDate,
                    String status, BigDecimal totalAmount, String shippingAddress) {
        this.orderCode = orderCode;
        this.username = username;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
    }

    public String getOrderCode() { return orderCode; }
    public void setOrderCode(String orderCode) { this.orderCode = orderCode; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}